// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungDetails;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTreffer;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTrefferItem;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungPayload;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungselementPayload;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.generatoren.AufgabensammlungLaTeXGeneratorService;
import de.egladil.mja_api.domain.generatoren.AufgabensammlungPDFGeneratorService;
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.AufgabensammlungDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesAufgabensammlugnselement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * AufgabensammlungenService
 */
@ApplicationScoped
public class AufgabensammlungenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabensammlungenService.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	AufgabensammlungDao aufgabensammlungDao;

	@Inject
	RaetselService raetselService;

	@Inject
	QuizService quizService;

	@Inject
	AufgabensammlungPDFGeneratorService aufgabensammlungPDFGenerator;

	@Inject
	AufgabensammlungLaTeXGeneratorService aufgabensammlungLaTeXGenerator;

	public AufgabensammlungSucheTreffer findAufgabensammlungen(final AufgabensammlungenSuchparameter suchparameter, final int limit, final int offset) {

		AufgabensammlungSucheTreffer result = new AufgabensammlungSucheTreffer();
		long anzahlGesamt = aufgabensammlungDao.countByFilter(suchparameter);

		if (anzahlGesamt == 0L) {

			return result;
		}

		List<PersistenteAufgabensammlung> trefferliste = aufgabensammlungDao.findByFilter(suchparameter, limit, offset);

		for (PersistenteAufgabensammlung treffer : trefferliste) {

			long anzahlElemente = aufgabensammlungDao.countElementeAufgabensammlung(treffer.uuid);

			AufgabensammlungSucheTrefferItem item = mapFromDB(treffer);
			item.setAnzahlElemente(anzahlElemente);
			result.addItem(item);
		}

		result.setTrefferGesamt(anzahlGesamt);
		return result;
	}

	/**
	 * @param  aufgabensammlungID
	 * @param  userId
	 *                            String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                            boolean
	 * @return                    Optional
	 */
	public Optional<AufgabensammlungDetails> loadDetails(final String aufgabensammlungID) {

		PersistenteAufgabensammlung aufgabensammlung = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (aufgabensammlung == null) {

			return Optional.empty();
		}

		final AufgabensammlungDetails result = AufgabensammlungDetails.createFromDB(aufgabensammlung);

		if (PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndAufgabensammlungenPermission(authCtx), aufgabensammlung.owner)) {

			result.markiereAlsAenderbar();
		}

		List<PersistentesAufgabensammlugnselement> elementeDB = aufgabensammlungDao
			.loadElementeAufgabensammlung(aufgabensammlungID);
		List<PersistenteAufgabeReadonly> aufgaben = aufgabensammlungDao.loadAufgabenByAufgabensammlung(aufgabensammlungID);

		elementeDB.forEach(r -> {

			Optional<PersistenteAufgabeReadonly> optAufgabe = aufgaben.stream().filter(a -> a.uuid.equals(r.raetselID)).findFirst();

			if (optAufgabe.isPresent()) {

				result.addElement(Aufgabensammlungselement.merge(optAufgabe.get(), r));
			}
		});

		result.sortElemente();
		return Optional.of(result);
	}

	/**
	 * Erstellt eine neue Aufgabensammlung. Dabei wird geprüft, ob es eine mit den Keys bereits gibt oder dem Namen. In diesem Fall
	 * wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  userId
	 *                 String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                 boolean
	 * @return         AufgabensammlungSucheTrefferItem
	 */
	@Transactional
	public AufgabensammlungSucheTrefferItem aufgabensammlungAnlegen(final EditAufgabensammlungPayload payload) throws WebApplicationException {

		String userId = authCtx.getUser().getName();

		if (!AbstractDomainEntity.UUID_NEUE_ENTITY.equals(payload.getId())) {

			LOGGER.error("POST mit vorhandener Entity uuid={} aufgerufen", payload.getId());

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("POST darf nur mit id='neu' aufgerufen werden"))
					.build());
		}

		if (dupletteNachKeysExistiert(payload)) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Aufgabensammlung mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Aufgabensammlung mit diesem Namen."))
					.build());
		}

		PersistenteAufgabensammlung aufgabensammlung = new PersistenteAufgabensammlung();
		mergeFromPayload(aufgabensammlung, payload);
		aufgabensammlung.geaendertDurch = userId;
		aufgabensammlung.freigegeben = false;
		aufgabensammlung.owner = userId;

		PersistenteAufgabensammlung persistierte = speichern(aufgabensammlung);
		AufgabensammlungSucheTrefferItem result = mapFromDB(persistierte);

		LOGGER.info("Aufgabensammlung angelegt: {}, admin={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	/**
	 * Ändert die Basisdaten einer Aufgabensammlung. Dabei wird geprüft, ob es eine mit den Keys oder dem Namen bereits gibt. In
	 * diesem Fall wird eine WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  userId
	 *                 String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                 boolean
	 * @return         AufgabensammlungSucheTrefferItem
	 */
	@Transactional
	public AufgabensammlungSucheTrefferItem aufgabensammlungBasisdatenAendern(final EditAufgabensammlungPayload payload) throws WebApplicationException {

		String userId = authCtx.getUser().getName();

		if (dupletteNachKeysExistiert(payload)) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Aufgabensammlung mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Aufgabensammlung mit diesem Namen."))
					.build());
		}

		PersistenteAufgabensammlung ausDB = aufgabensammlungDao.findByID(payload.getId());

		if (ausDB == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND)
					.entity(MessagePayload.error("Diese Aufgabensammlung gibt es nicht."))
					.build());
		}

		checkPermission(ausDB);

		mergeFromPayload(ausDB, payload);
		ausDB.geaendertDurch = userId;

		PersistenteAufgabensammlung persistierte = speichern(ausDB);

		AufgabensammlungSucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = aufgabensammlungDao.countElementeAufgabensammlung(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Aufgabensammlung geaendert: {}, admin={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	PersistenteAufgabensammlung speichern(final PersistenteAufgabensammlung aufgabensammlung) {

		return aufgabensammlungDao.saveAufgabensammlung(aufgabensammlung);
	}

	boolean dupletteNachKeysExistiert(final EditAufgabensammlungPayload payload) {

		if (payload.getReferenztyp() == null) {

			return false;
		}

		PersistenteAufgabensammlung persistente = aufgabensammlungDao.findByUniqueKey(payload.getReferenztyp(),
			payload.getReferenz(), payload.getSchwierigkeitsgrad());

		if (persistente == null) {

			return false;
		}

		return !persistente.uuid.equals(payload.getId());

	}

	boolean namensdubletteExistiert(final String name, final String uuid) {

		PersistenteAufgabensammlung persistente = aufgabensammlungDao.findByName(name);

		if (persistente == null) {

			return false;
		}

		return !uuid.equals(persistente.uuid);
	}

	AufgabensammlungSucheTrefferItem mapFromDB(final PersistenteAufgabensammlung ausDB) {

		AufgabensammlungSucheTrefferItem result = new AufgabensammlungSucheTrefferItem();
		result.setId(ausDB.uuid);
		result.setKommentar(ausDB.kommentar);
		result.setName(ausDB.name);
		result.setReferenz(ausDB.referenz);
		result.setReferenztyp(ausDB.referenztyp);
		result.setSchwierigkeitsgrad(ausDB.schwierigkeitsgrad);
		result.setFreigegeben(ausDB.freigegeben);
		result.setPrivat(ausDB.privat);
		result.setGeaendertDurch(ausDB.geaendertDurch);

		return result;

	}

	void mergeFromPayload(final PersistenteAufgabensammlung aufgabensammlung, final EditAufgabensammlungPayload payload) {

		aufgabensammlung.kommentar = payload.getKommentar();
		aufgabensammlung.name = payload.getName();
		aufgabensammlung.referenz = payload.getReferenz();
		aufgabensammlung.referenztyp = payload.getReferenztyp();
		aufgabensammlung.schwierigkeitsgrad = payload.getSchwierigkeitsgrad();
		aufgabensammlung.freigegeben = payload.isFreigegeben();
		aufgabensammlung.privat = payload.isPrivat();
	}

	/**
	 * Legt ein neues Element an
	 *
	 * @param  aufgabensammlungID
	 * @param  payload
	 * @return                    AufgabensammlungDetails
	 */
	public AufgabensammlungDetails elementAnlegen(final String aufgabensammlungID, final EditAufgabensammlungselementPayload payload) {

		PersistenteAufgabensammlung aufgabensammlung = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (aufgabensammlung == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Aufgabensammlung gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(aufgabensammlung);

		Optional<String> optRaetselId = raetselService.getRaetselIdWithSchluessel(payload.getRaetselSchluessel());

		if (optRaetselId.isEmpty()) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, mit dem gewünschen Schlüssel gibt es gar kein Rätsel."))
				.build();
			throw new WebApplicationException(response);
		}

		List<PersistentesAufgabensammlugnselement> persistenteElemente = aufgabensammlungDao
			.loadElementeAufgabensammlung(aufgabensammlungID);

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Aufgabensammlung gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		final String raetselUuid = optRaetselId.get();

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleichemRaetsel = persistenteElemente.stream()
			.filter(el -> el.raetselID.equals(raetselUuid)).findFirst();

		if (optElementMitGleichemRaetsel.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Das Rätsel gibt es in dieser Aufgabensammlung schon."))
				.build();
			throw new WebApplicationException(response);

		}

		this.createAndPersistNeuesElement(aufgabensammlungID,
			optRaetselId.get(), payload);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(aufgabensammlungID);

		if (opt.isEmpty()) {

			LOGGER.error("Aufgabensammlung mit der UUID={} wurde ein paar Zeilen später nicht mehr gefunden", aufgabensammlungID);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(MessagePayload.error("Ups, da ist aber etwas komplett schiefgelaufen"))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	@Transactional
	PersistentesAufgabensammlugnselement createAndPersistNeuesElement(final String aufgabensammlungID, final String raetselID, final EditAufgabensammlungselementPayload payload) {

		PersistentesAufgabensammlugnselement neues = new PersistentesAufgabensammlugnselement();
		neues.nummer = payload.getNummer();
		neues.punkte = payload.getPunkte();
		neues.aufgabensammlungID = aufgabensammlungID;
		neues.raetselID = raetselID;

		PersistentesAufgabensammlugnselement persisted = aufgabensammlungDao.saveElement(neues);

		return persisted;
	}

	/**
	 * Ändert ein vorhandenes Element
	 *
	 * @param  aufgabensammlungID
	 * @param  payload
	 * @return                    AufgabensammlungDetails
	 */
	@Transactional
	public AufgabensammlungDetails elementAendern(final String aufgabensammlungID, final EditAufgabensammlungselementPayload payload) {

		PersistentesAufgabensammlugnselement persistentesElement = aufgabensammlungDao.findElementById(payload.getId());

		if (persistentesElement == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, dieses Element gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		PersistenteAufgabensammlung aufgabensammlung = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (aufgabensammlung == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Aufgabensammlung gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(aufgabensammlung);

		if (!aufgabensammlungID.equals(persistentesElement.aufgabensammlungID)) {

			LOGGER.error("Konflikt: persistentesElement.aufgabensammlungID={}, aufgabensammlungID={}",
				persistentesElement.aufgabensammlungID, aufgabensammlungID);

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Konflikt"))
				.build();
			throw new WebApplicationException(response);
		}

		List<PersistentesAufgabensammlugnselement> persistenteElemente = aufgabensammlungDao
			.loadElementeAufgabensammlung(aufgabensammlungID);

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer()) && !el.uuid.equals(payload.getId())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Aufgabensammlung gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		mergeAndSaveElement(persistentesElement, payload);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(aufgabensammlungID);

		if (opt.isEmpty()) {

			LOGGER.error("Aufgabensammlung mit der UUID={} wurde ein paar Zeilen später nicht mehr gefunden", aufgabensammlungID);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(MessagePayload.error("Tja, diese Aufgabensammlung gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	void mergeAndSaveElement(final PersistentesAufgabensammlugnselement persistentesElement, final EditAufgabensammlungselementPayload payload) {

		persistentesElement.nummer = payload.getNummer();
		persistentesElement.punkte = payload.getPunkte();

		aufgabensammlungDao.saveElement(persistentesElement);
	}

	/**
	 * Löscht das gegebene Element der Aufgabensammlung.
	 *
	 * @param  aufgabensammlungID
	 * @param  elementID
	 * @return                    AufgabensammlungDetails
	 */
	public AufgabensammlungDetails elementLoeschen(final String aufgabensammlungID, final String elementID) {

		PersistenteAufgabensammlung aufgabensammlung = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (aufgabensammlung != null) {

			checkPermission(aufgabensammlung);
		}

		aufgabensammlungDao.deleteElement(elementID);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(aufgabensammlungID);

		if (opt.isEmpty()) {

			LOGGER.error("Aufgabensammlung mit der UUID={} gibt es nicht", aufgabensammlungID);
			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Ups, da ist aber etwas komplett schiefgelaufen"))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	/**
	 * Generiert die Vorschau des Quiz als PDF. Dabei werden Aufgaben und Lösungen gemischt.
	 * Bei Aufgaben ohne Antwortvorschläge wird keine Tabelle gedruckt.
	 *
	 * @param  aufgabensammlungID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return
	 */
	public GeneratedFile printVorschau(final String aufgabensammlungID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Aufgabensammlung gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(aufgabensammlungID);

		AufgabensammlungGeneratorInput input = createAufgabensammlungGeneratorInput(Verwendungszweck.VORSCHAU, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, aufgaben);

		return aufgabensammlungPDFGenerator.generate(input);
	}

	/**
	 * Generiert eine Kartei. Für jedes Element wird auf eine Seite die Frage gedruckt, auf die folgende Seite die Lösung.
	 *
	 * @param  aufgabensammlungID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	public GeneratedFile printKartei(final String aufgabensammlungID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(aufgabensammlungID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		AufgabensammlungGeneratorInput input = createAufgabensammlungGeneratorInput(Verwendungszweck.KARTEI, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, freigegebeneAufgaben);

		return aufgabensammlungPDFGenerator.generate(input);
	}

	/**
	 * Generiert eine PDF-Datei mit Aufgabenblättern und Lösungen. Zuerst kommen die Aufgaben, danach, beginnend mit einer neuen
	 * Seite, die Lösungen in der gewünschten Reihenfolge. Es wird generell ohne Auntwortvorschläge gedruckt.
	 *
	 * @param  aufgabensammlungID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	public GeneratedFile printArbeitsblattMitLoesungen(final String aufgabensammlungID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(aufgabensammlungID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		AufgabensammlungGeneratorInput input = createAufgabensammlungGeneratorInput(Verwendungszweck.ARBEITSBLATT, font,
			schriftgroesse,
			layoutAntwortvorschlaege, dbResult, freigegebeneAufgaben);

		return aufgabensammlungPDFGenerator.generate(input);
	}

	List<Quizaufgabe> vorbedingungenPublicResourcesPruefen(final PersistenteAufgabensammlung dbResult) throws WebApplicationException {

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Aufgabensammlung gibt es nicht.")).build());
		}

		checkPermission(dbResult);

		String aufgabensammlungID = dbResult.uuid;

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(aufgabensammlungID);
		List<Quizaufgabe> freigegebeneAufgaben = aufgaben;

		if (PermissionUtils.isUserOrdinary(authCtx.getUser().getRoles())) {

			freigegebeneAufgaben = aufgaben.stream().filter(a -> a.isFreigegeben())
				.collect(Collectors.toList());

			if (freigegebeneAufgaben.isEmpty()) {

				LOGGER.error("Aufgabensammlung {} - {} hat keine freigegebenen Aufgaben. Aufruf durch admin {}", aufgabensammlungID,
					dbResult.name, StringUtils.abbreviate(authCtx.getUser().getUuid(), 11));

				throw new WebApplicationException(
					Response.status(Status.FORBIDDEN)
						.entity(MessagePayload.error("Drucken einer Kartei nicht erlaubt. Keine freigegebenen Rätsel.")).build());
			}

		}

		if (freigegebeneAufgaben.isEmpty()) {

			LOGGER.error("Aufgabensammlung {} - {} hat keine Aufgaben. Aufruf durch admin {}", aufgabensammlungID,
				dbResult.name, StringUtils.abbreviate(authCtx.getUser().getUuid(), 11));

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST)
					.entity(MessagePayload.error("Drucken einer Kartei nicht möglich. Aufgabensammlung ist leer.")).build());
		}

		return freigegebeneAufgaben;
	}

	/**
	 * Generiert das LaTeX-File für die Aufgabensammlung. Die Grafiken muss man sowieso lokal haben. Sollte sich mit kleineren
	 * Textreplacements lokal compilieren lassen.
	 *
	 * @param  aufgabensammlungID
	 * @return                    GeneratedFile
	 */
	public File downloadLaTeXSources(final String aufgabensammlungID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(aufgabensammlungID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Aufgabensammlung gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(aufgabensammlungID);

		AufgabensammlungGeneratorInput input = createAufgabensammlungGeneratorInput(Verwendungszweck.LATEX, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, aufgaben);

		return aufgabensammlungLaTeXGenerator.generateLaTeXArchive(input);
	}

	/**
	 * @param  font
	 * @param  schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 * @param  dbResult
	 * @param  aufgaben
	 * @return
	 */
	private AufgabensammlungGeneratorInput createAufgabensammlungGeneratorInput(final Verwendungszweck verwendungszweck, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final PersistenteAufgabensammlung dbResult, final List<Quizaufgabe> aufgaben) {

		AufgabensammlungGeneratorInput input = new AufgabensammlungGeneratorInput()
			.withAufgaben(aufgaben)
			.withFont(font)
			.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege)
			.withAufgabensammlung(dbResult)
			.withVerwendungszweck(verwendungszweck)
			.withSchriftgroesse(schriftgroesse);
		return input;
	}

	void checkPermission(final PersistenteAufgabensammlung ausDB) {

		if (!PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndAufgabensammlungenPermission(authCtx), ausDB.owner)) {

			LOGGER.warn("User {} hat versucht, Aufgabensammlung {} mit Owner {} zu aendern oder zu drucken",
				authCtx.getUser().getName(), ausDB.uuid,
				ausDB.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}
	}

	GeneratedFile mapToGeneratedFile(final File file) {

		byte[] bytes = MjaFileUtils.loadBinaryFile(file.getAbsolutePath(), false);

		GeneratedFile result = new GeneratedFile();
		result.setFileData(bytes);
		result.setFileName(file.getName());
		return result;
	}
}
