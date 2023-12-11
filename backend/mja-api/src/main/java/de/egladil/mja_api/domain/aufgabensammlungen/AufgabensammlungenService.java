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
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.RaetselgruppeLaTeXGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselgruppePDFGeneratorService;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
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
	RaetselgruppePDFGeneratorService raetselgruppePDFGenerator;

	@Inject
	RaetselgruppeLaTeXGeneratorService raetselgruppenLaTeXGenerator;

	public AufgabensammlungSucheTreffer findRaetselgruppen(final AufgabensammlungenSuchparameter suchparameter, final int limit, final int offset) {

		AufgabensammlungSucheTreffer result = new AufgabensammlungSucheTreffer();
		long anzahlGesamt = aufgabensammlungDao.countByFilter(suchparameter);

		if (anzahlGesamt == 0L) {

			return result;
		}

		List<PersistenteAufgabensammlung> trefferliste = aufgabensammlungDao.findByFilter(suchparameter, limit, offset);

		for (PersistenteAufgabensammlung treffer : trefferliste) {

			long anzahlElemente = aufgabensammlungDao.countElementeRaetselgruppe(treffer.uuid);

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
	public Optional<AufgabensammlungDetails> loadDetails(final String raetselgruppeID) {

		PersistenteAufgabensammlung raetselgruppe = aufgabensammlungDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			return Optional.empty();
		}

		final AufgabensammlungDetails result = AufgabensammlungDetails.createFromDB(raetselgruppe);

		if (PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), raetselgruppe.owner)) {

			result.markiereAlsAenderbar();
		}

		List<PersistentesAufgabensammlugnselement> elementeDB = aufgabensammlungDao.loadElementeRaetselgruppe(raetselgruppeID);
		List<PersistenteAufgabeReadonly> aufgaben = aufgabensammlungDao.loadAufgabenByReaetselgruppe(raetselgruppeID);

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
	 * Erstellt eine neue Rätselgruppe. Dabei wird geprüft, ob es eine mit den Keys bereits gibt oder dem Namen. In diesem Fall
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
	public AufgabensammlungSucheTrefferItem raetselgruppeAnlegen(final EditAufgabensammlungPayload payload) throws WebApplicationException {

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
					.entity(MessagePayload.error("Es gibt bereits eine Rätselgruppe mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselgruppe mit diesem Namen."))
					.build());
		}

		PersistenteAufgabensammlung raetselgruppe = new PersistenteAufgabensammlung();
		mergeFromPayload(raetselgruppe, payload);
		raetselgruppe.geaendertDurch = userId;
		raetselgruppe.freigegeben = false;
		raetselgruppe.owner = userId;

		PersistenteAufgabensammlung persistierte = speichern(raetselgruppe);
		AufgabensammlungSucheTrefferItem result = mapFromDB(persistierte);

		LOGGER.info("Rätselgruppe angelegt: {}, admin={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	/**
	 * Ändert die Basisdaten einer Rätselgruppe. Dabei wird geprüft, ob es eine mit den Keys oder dem Namen bereits gibt. In
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
	public AufgabensammlungSucheTrefferItem raetselgruppeBasisdatenAendern(final EditAufgabensammlungPayload payload) throws WebApplicationException {

		String userId = authCtx.getUser().getName();

		if (dupletteNachKeysExistiert(payload)) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselgruppe mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselgruppe mit diesem Namen."))
					.build());
		}

		PersistenteAufgabensammlung ausDB = aufgabensammlungDao.findByID(payload.getId());

		if (ausDB == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND)
					.entity(MessagePayload.error("Diese Rätselgruppe gibt es nicht."))
					.build());
		}

		checkPermission(ausDB);

		mergeFromPayload(ausDB, payload);
		ausDB.geaendertDurch = userId;

		PersistenteAufgabensammlung persistierte = speichern(ausDB);

		AufgabensammlungSucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = aufgabensammlungDao.countElementeRaetselgruppe(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselgruppe geaendert: {}, admin={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	PersistenteAufgabensammlung speichern(final PersistenteAufgabensammlung raetselgruppe) {

		return aufgabensammlungDao.saveRaetselgruppe(raetselgruppe);
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

	void mergeFromPayload(final PersistenteAufgabensammlung raetselgruppe, final EditAufgabensammlungPayload payload) {

		raetselgruppe.kommentar = payload.getKommentar();
		raetselgruppe.name = payload.getName();
		raetselgruppe.referenz = payload.getReferenz();
		raetselgruppe.referenztyp = payload.getReferenztyp();
		raetselgruppe.schwierigkeitsgrad = payload.getSchwierigkeitsgrad();
		raetselgruppe.freigegeben = payload.isFreigegeben();
		raetselgruppe.privat = payload.isPrivat();
	}

	/**
	 * Legt ein neues Element an
	 *
	 * @param  aufgabensammlungID
	 * @param  payload
	 * @return                    AufgabensammlungDetails
	 */
	public AufgabensammlungDetails elementAnlegen(final String raetselgruppeID, final EditAufgabensammlungselementPayload payload) {

		PersistenteAufgabensammlung raetselgruppe = aufgabensammlungDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(raetselgruppe);

		Optional<String> optRaetselId = raetselService.getRaetselIdWithSchluessel(payload.getRaetselSchluessel());

		if (optRaetselId.isEmpty()) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, mit dem gewünschen Schlüssel gibt es gar kein Rätsel."))
				.build();
			throw new WebApplicationException(response);
		}

		List<PersistentesAufgabensammlugnselement> persistenteElemente = aufgabensammlungDao
			.loadElementeRaetselgruppe(raetselgruppeID);

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		final String raetselUuid = optRaetselId.get();

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleichemRaetsel = persistenteElemente.stream()
			.filter(el -> el.raetselID.equals(raetselUuid)).findFirst();

		if (optElementMitGleichemRaetsel.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Das Rätsel gibt es in dieser Rätselgruppe schon."))
				.build();
			throw new WebApplicationException(response);

		}

		this.createAndPersistNeuesRaetselgruppenelement(raetselgruppeID,
			optRaetselId.get(), payload);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(raetselgruppeID);

		if (opt.isEmpty()) {

			LOGGER.error("Raetselgruppe mit der UUID={} wurde ein paar Zeilen später nicht mehr gefunden", raetselgruppeID);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(MessagePayload.error("Ups, da ist aber etwas komplett schiefgelaufen"))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	@Transactional
	PersistentesAufgabensammlugnselement createAndPersistNeuesRaetselgruppenelement(final String raetselgruppeID, final String raetselID, final EditAufgabensammlungselementPayload payload) {

		PersistentesAufgabensammlugnselement neues = new PersistentesAufgabensammlugnselement();
		neues.nummer = payload.getNummer();
		neues.punkte = payload.getPunkte();
		neues.aufgabensammlungID = raetselgruppeID;
		neues.raetselID = raetselID;

		PersistentesAufgabensammlugnselement persisted = aufgabensammlungDao.saveRaetselgruppenelement(neues);

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
	public AufgabensammlungDetails elementAendern(final String raetselgruppeID, final EditAufgabensammlungselementPayload payload) {

		PersistentesAufgabensammlugnselement persistentesElement = aufgabensammlungDao.findElementById(payload.getId());

		if (persistentesElement == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, dieses Rätselgruppenelement gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		PersistenteAufgabensammlung raetselgruppe = aufgabensammlungDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(raetselgruppe);

		if (!raetselgruppeID.equals(persistentesElement.aufgabensammlungID)) {

			LOGGER.error("Raetselgruppenkonflikt: persistentesElement.raetselgruppeID={}, aufgabensammlungID={}",
				persistentesElement.aufgabensammlungID, raetselgruppeID);

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Rätselgruppenkonflikt"))
				.build();
			throw new WebApplicationException(response);
		}

		List<PersistentesAufgabensammlugnselement> persistenteElemente = aufgabensammlungDao
			.loadElementeRaetselgruppe(raetselgruppeID);

		Optional<PersistentesAufgabensammlugnselement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer()) && !el.uuid.equals(payload.getId())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		mergeAndSaveRaetselgruppenelement(persistentesElement, payload);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(raetselgruppeID);

		if (opt.isEmpty()) {

			LOGGER.error("Raetselgruppe mit der UUID={} wurde ein paar Zeilen später nicht mehr gefunden", raetselgruppeID);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	void mergeAndSaveRaetselgruppenelement(final PersistentesAufgabensammlugnselement persistentesElement, final EditAufgabensammlungselementPayload payload) {

		persistentesElement.nummer = payload.getNummer();
		persistentesElement.punkte = payload.getPunkte();

		aufgabensammlungDao.saveRaetselgruppenelement(persistentesElement);
	}

	/**
	 * Löscht das gegebene Element der Rätselgruppe.
	 *
	 * @param  aufgabensammlungID
	 * @param  elementID
	 * @return                    AufgabensammlungDetails
	 */
	public AufgabensammlungDetails elementLoeschen(final String raetselgruppeID, final String elementID) {

		PersistenteAufgabensammlung raetselgruppe = aufgabensammlungDao.findByID(raetselgruppeID);

		if (raetselgruppe != null) {

			checkPermission(raetselgruppe);
		}

		aufgabensammlungDao.deleteRaetselgruppenelement(elementID);

		Optional<AufgabensammlungDetails> opt = this.loadDetails(raetselgruppeID);

		if (opt.isEmpty()) {

			LOGGER.error("Raetselgruppe mit der UUID={} gibt es nicht", raetselgruppeID);
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
	public GeneratedFile printVorschau(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		RaetselgruppeGeneratorInput input = createRaetselgruppeGeneratorInput(Verwendungszweck.VORSCHAU, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, aufgaben);

		return raetselgruppePDFGenerator.generate(input);
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
	public GeneratedFile printKartei(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		RaetselgruppeGeneratorInput input = createRaetselgruppeGeneratorInput(Verwendungszweck.KARTEI, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, freigegebeneAufgaben);

		return raetselgruppePDFGenerator.generate(input);
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
	public GeneratedFile printArbeitsblattMitLoesungen(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		RaetselgruppeGeneratorInput input = createRaetselgruppeGeneratorInput(Verwendungszweck.ARBEITSBLATT, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, freigegebeneAufgaben);

		return raetselgruppePDFGenerator.generate(input);
	}

	List<Quizaufgabe> vorbedingungenPublicResourcesPruefen(final PersistenteAufgabensammlung dbResult) throws WebApplicationException {

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		checkPermission(dbResult);

		String raetselgruppeID = dbResult.uuid;

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = aufgaben;

		if (PermissionUtils.isUserOrdinary(authCtx.getUser().getRoles())) {

			freigegebeneAufgaben = aufgaben.stream().filter(a -> a.isFreigebeben())
				.collect(Collectors.toList());

			if (freigegebeneAufgaben.isEmpty()) {

				LOGGER.error("Rätselgruppe {} - {} hat keine freigegebenen Aufgaben. Aufruf durch admin {}", raetselgruppeID,
					dbResult.name, StringUtils.abbreviate(authCtx.getUser().getUuid(), 11));

				throw new WebApplicationException(
					Response.status(Status.FORBIDDEN)
						.entity(MessagePayload.error("Drucken einer Kartei nicht erlaubt. Keine freigegebenen Rätsel.")).build());
			}

		}

		if (freigegebeneAufgaben.isEmpty()) {

			LOGGER.error("Rätselgruppe {} - {} hat keine Aufgaben. Aufruf durch admin {}", raetselgruppeID,
				dbResult.name, StringUtils.abbreviate(authCtx.getUser().getUuid(), 11));

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST)
					.entity(MessagePayload.error("Drucken einer Kartei nicht möglich. Rätselgruppe ist leer.")).build());
		}

		return freigegebeneAufgaben;
	}

	/**
	 * Generiert das LaTeX-File für die Raetselgruppe. Die Grafiken muss man sowieso lokal haben. Sollte sich mit kleineren
	 * Textreplacements lokal compilieren lassen.
	 *
	 * @param  aufgabensammlungID
	 * @return                    GeneratedFile
	 */
	public File downloadLaTeXSources(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		RaetselgruppeGeneratorInput input = createRaetselgruppeGeneratorInput(Verwendungszweck.LATEX, font, schriftgroesse,
			layoutAntwortvorschlaege, dbResult, aufgaben);

		return raetselgruppenLaTeXGenerator.generateLaTeXArchive(input);
	}

	/**
	 * @param  font
	 * @param  schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 * @param  dbResult
	 * @param  aufgaben
	 * @return
	 */
	private RaetselgruppeGeneratorInput createRaetselgruppeGeneratorInput(final Verwendungszweck verwendungszweck, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final PersistenteAufgabensammlung dbResult, final List<Quizaufgabe> aufgaben) {

		RaetselgruppeGeneratorInput input = new RaetselgruppeGeneratorInput()
			.withAufgaben(aufgaben)
			.withFont(font)
			.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege)
			.withRaetselgruppe(dbResult)
			.withVerwendungszweck(verwendungszweck)
			.withSchriftgroesse(schriftgroesse);
		return input;
	}

	void checkPermission(final PersistenteAufgabensammlung ausDB) {

		if (!PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), ausDB.owner)) {

			LOGGER.warn("User {} hat versucht, Raetselgruppe {} mit Owner {} zu aendern oder zu drucken",
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
