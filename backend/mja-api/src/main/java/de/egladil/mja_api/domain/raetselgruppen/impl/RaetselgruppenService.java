// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.RaetselgruppeGeneratorService;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselgruppenDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * RaetselgruppenService
 */
@ApplicationScoped
public class RaetselgruppenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppenService.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	RaetselgruppenDao raetselgruppenDao;

	@Inject
	RaetselService raetselService;

	@Inject
	QuizService quizService;

	@Inject
	RaetselgruppeGeneratorService raetselgruppeFileService;

	public RaetselgruppensucheTreffer findRaetselgruppen(final RaetselgruppenSuchparameter suchparameter, final int limit, final int offset) {

		RaetselgruppensucheTreffer result = new RaetselgruppensucheTreffer();
		long anzahlGesamt = raetselgruppenDao.countByFilter(suchparameter);

		if (anzahlGesamt == 0L) {

			return result;
		}

		List<PersistenteRaetselgruppe> trefferliste = raetselgruppenDao.findByFilter(suchparameter, limit, offset);

		for (PersistenteRaetselgruppe treffer : trefferliste) {

			long anzahlElemente = raetselgruppenDao.countElementeRaetselgruppe(treffer.uuid);

			RaetselgruppensucheTrefferItem item = mapFromDB(treffer);
			item.setAnzahlElemente(anzahlElemente);
			result.addItem(item);
		}

		result.setTrefferGesamt(anzahlGesamt);
		return result;
	}

	/**
	 * @param  raetselgruppeID
	 * @param  userId
	 *                         String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                         boolean
	 * @return                 Optional
	 */
	public Optional<RaetselgruppeDetails> loadDetails(final String raetselgruppeID) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			return Optional.empty();
		}

		final RaetselgruppeDetails result = RaetselgruppeDetails.createFromDB(raetselgruppe);

		if (PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), raetselgruppe.owner)) {

			result.markiereAlsAenderbar();
		}

		List<PersistentesRaetselgruppenelement> elementeDB = raetselgruppenDao.loadElementeRaetselgruppe(raetselgruppeID);
		List<PersistenteAufgabeReadonly> aufgaben = raetselgruppenDao.loadAufgabenByReaetselgruppe(raetselgruppeID);

		elementeDB.forEach(r -> {

			Optional<PersistenteAufgabeReadonly> optAufgabe = aufgaben.stream().filter(a -> a.uuid.equals(r.raetselID)).findFirst();

			if (optAufgabe.isPresent()) {

				result.addElement(Raetselgruppenelement.merge(optAufgabe.get(), r));
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
	 * @return         RaetselgruppensucheTrefferItem
	 */
	@Transactional
	public RaetselgruppensucheTrefferItem raetselgruppeAnlegen(final EditRaetselgruppePayload payload) throws WebApplicationException {

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

		PersistenteRaetselgruppe raetselgruppe = new PersistenteRaetselgruppe();
		mergeFromPayload(raetselgruppe, payload);
		raetselgruppe.geaendertDurch = userId;
		raetselgruppe.status = DomainEntityStatus.ERFASST;
		raetselgruppe.owner = userId;

		PersistenteRaetselgruppe persistierte = speichern(raetselgruppe);
		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

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
	 * @return         RaetselgruppensucheTrefferItem
	 */
	@Transactional
	public RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(final EditRaetselgruppePayload payload) throws WebApplicationException {

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

		PersistenteRaetselgruppe ausDB = raetselgruppenDao.findByID(payload.getId());

		if (ausDB == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND)
					.entity(MessagePayload.error("Diese Rätselgruppe gibt es nicht."))
					.build());
		}

		checkPermission(ausDB);

		mergeFromPayload(ausDB, payload);
		ausDB.geaendertDurch = userId;

		PersistenteRaetselgruppe persistierte = speichern(ausDB);

		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = raetselgruppenDao.countElementeRaetselgruppe(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselgruppe geaendert: {}, admin={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	PersistenteRaetselgruppe speichern(final PersistenteRaetselgruppe raetselgruppe) {

		return raetselgruppenDao.saveRaetselgruppe(raetselgruppe);
	}

	boolean dupletteNachKeysExistiert(final EditRaetselgruppePayload payload) {

		if (payload.getReferenztyp() == null) {

			return false;
		}

		PersistenteRaetselgruppe persistente = raetselgruppenDao.findByUniqueKey(payload.getReferenztyp(),
			payload.getReferenz(), payload.getSchwierigkeitsgrad());

		if (persistente == null) {

			return false;
		}

		return !persistente.uuid.equals(payload.getId());

	}

	boolean namensdubletteExistiert(final String name, final String uuid) {

		PersistenteRaetselgruppe persistente = raetselgruppenDao.findByName(name);

		if (persistente == null) {

			return false;
		}

		return !uuid.equals(persistente.uuid);
	}

	RaetselgruppensucheTrefferItem mapFromDB(final PersistenteRaetselgruppe ausDB) {

		RaetselgruppensucheTrefferItem result = new RaetselgruppensucheTrefferItem();
		result.setId(ausDB.uuid);
		result.setKommentar(ausDB.kommentar);
		result.setName(ausDB.name);
		result.setReferenz(ausDB.referenz);
		result.setReferenztyp(ausDB.referenztyp);
		result.setSchwierigkeitsgrad(ausDB.schwierigkeitsgrad);
		result.setStatus(ausDB.status);
		result.setGeaendertDurch(ausDB.geaendertDurch);

		return result;

	}

	void mergeFromPayload(final PersistenteRaetselgruppe raetselgruppe, final EditRaetselgruppePayload payload) {

		raetselgruppe.kommentar = payload.getKommentar();
		raetselgruppe.name = payload.getName();
		raetselgruppe.referenz = payload.getReferenz();
		raetselgruppe.referenztyp = payload.getReferenztyp();
		raetselgruppe.schwierigkeitsgrad = payload.getSchwierigkeitsgrad();
		raetselgruppe.status = payload.getStatus();
	}

	/**
	 * Legt ein neues Element an
	 *
	 * @param  raetselgruppeID
	 * @param  payload
	 * @return                 RaetselgruppeDetails
	 */
	public RaetselgruppeDetails elementAnlegen(final String raetselgruppeID, final EditRaetselgruppenelementPayload payload) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

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

		List<PersistentesRaetselgruppenelement> persistenteElemente = raetselgruppenDao.loadElementeRaetselgruppe(raetselgruppeID);

		Optional<PersistentesRaetselgruppenelement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		final String raetselUuid = optRaetselId.get();

		Optional<PersistentesRaetselgruppenelement> optElementMitGleichemRaetsel = persistenteElemente.stream()
			.filter(el -> el.raetselID.equals(raetselUuid)).findFirst();

		if (optElementMitGleichemRaetsel.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Das Rätsel gibt es in dieser Rätselgruppe schon."))
				.build();
			throw new WebApplicationException(response);

		}

		this.createAndPersistNeuesRaetselgruppenelement(raetselgruppeID,
			optRaetselId.get(), payload);

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID);

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
	PersistentesRaetselgruppenelement createAndPersistNeuesRaetselgruppenelement(final String raetselgruppeID, final String raetselID, final EditRaetselgruppenelementPayload payload) {

		PersistentesRaetselgruppenelement neues = new PersistentesRaetselgruppenelement();
		neues.nummer = payload.getNummer();
		neues.punkte = payload.getPunkte();
		neues.raetselgruppeID = raetselgruppeID;
		neues.raetselID = raetselID;

		PersistentesRaetselgruppenelement persisted = raetselgruppenDao.saveRaetselgruppenelement(neues);

		return persisted;
	}

	/**
	 * Ändert ein vorhandenes Element
	 *
	 * @param  raetselgruppeID
	 * @param  payload
	 * @return                 RaetselgruppeDetails
	 */
	@Transactional
	public RaetselgruppeDetails elementAendern(final String raetselgruppeID, final EditRaetselgruppenelementPayload payload) {

		PersistentesRaetselgruppenelement persistentesElement = raetselgruppenDao.findElementById(payload.getId());

		if (persistentesElement == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, dieses Rätselgruppenelement gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(raetselgruppe);

		if (!raetselgruppeID.equals(persistentesElement.raetselgruppeID)) {

			LOGGER.error("Raetselgruppenkonflikt: persistentesElement.raetselgruppeID={}, raetselgruppeID={}",
				persistentesElement.raetselgruppeID, raetselgruppeID);

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("Rätselgruppenkonflikt"))
				.build();
			throw new WebApplicationException(response);
		}

		List<PersistentesRaetselgruppenelement> persistenteElemente = raetselgruppenDao.loadElementeRaetselgruppe(raetselgruppeID);

		Optional<PersistentesRaetselgruppenelement> optElementMitGleicherNummer = persistenteElemente.stream()
			.filter(el -> el.nummer.equalsIgnoreCase(payload.getNummer()) && !el.uuid.equals(payload.getId())).findFirst();

		if (optElementMitGleicherNummer.isPresent()) {

			Response response = Response.status(Status.CONFLICT)
				.entity(MessagePayload.error("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer"))
				.build();
			throw new WebApplicationException(response);

		}

		mergeAndSaveRaetselgruppenelement(persistentesElement, payload);

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID);

		if (opt.isEmpty()) {

			LOGGER.error("Raetselgruppe mit der UUID={} wurde ein paar Zeilen später nicht mehr gefunden", raetselgruppeID);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	void mergeAndSaveRaetselgruppenelement(final PersistentesRaetselgruppenelement persistentesElement, final EditRaetselgruppenelementPayload payload) {

		persistentesElement.nummer = payload.getNummer();
		persistentesElement.punkte = payload.getPunkte();

		raetselgruppenDao.saveRaetselgruppenelement(persistentesElement);
	}

	/**
	 * Löscht das gegebene Element der Rätselgruppe.
	 *
	 * @param  raetselgruppeID
	 * @param  elementID
	 * @return                 RaetselgruppeDetails
	 */
	public RaetselgruppeDetails elementLoeschen(final String raetselgruppeID, final String elementID) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe != null) {

			checkPermission(raetselgruppe);
		}

		raetselgruppenDao.deleteRaetselgruppenelement(elementID);

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID);

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
	 * @param  raetselgruppeID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return
	 */
	public GeneratedFile printVorschau(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		return raetselgruppeFileService.generate(dbResult, aufgaben, Verwendungszweck.VORSCHAU, font,
			schriftgroesse, layoutAntwortvorschlaege);
	}

	/**
	 * Generiert eine Kartei. Für jedes Element wird auf eine Seite die Frage gedruckt, auf die folgende Seite die Lösung.
	 *
	 * @param  raetselgruppeID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	public GeneratedFile printKartei(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		return raetselgruppeFileService.generate(dbResult, freigegebeneAufgaben, Verwendungszweck.KARTEI, font,
			schriftgroesse, layoutAntwortvorschlaege);
	}

	/**
	 * Generiert eine PDF-Datei mit Aufgabenblättern und Lösungen. Zuerst kommen die Aufgaben, danach, beginnend mit einer neuen
	 * Seite, die Lösungen in der gewünschten Reihenfolge. Es wird generell ohne Auntwortvorschläge gedruckt.
	 *
	 * @param  raetselgruppeID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	public GeneratedFile printArbeitsblattMitLoesungen(final String raetselgruppeID, final FontName font, final Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = vorbedingungenPublicResourcesPruefen(dbResult);

		return raetselgruppeFileService.generate(dbResult, freigegebeneAufgaben, Verwendungszweck.ARBEITSBLATT, font,
			schriftgroesse, layoutAntwortvorschlaege);
	}

	List<Quizaufgabe> vorbedingungenPublicResourcesPruefen(final PersistenteRaetselgruppe dbResult) throws WebApplicationException {

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		checkPermission(dbResult);

		String raetselgruppeID = dbResult.uuid;

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);
		List<Quizaufgabe> freigegebeneAufgaben = aufgaben;

		if (PermissionUtils.isUserOrdinary(authCtx.getUser().getRoles())) {

			freigegebeneAufgaben = aufgaben.stream().filter(a -> DomainEntityStatus.FREIGEGEBEN == a.getStatus())
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
	 * @param  raetselgruppeID
	 * @return                 GeneratedFile
	 */
	public GeneratedFile downloadLaTeXSources(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		return raetselgruppeFileService.generate(dbResult, aufgaben, Verwendungszweck.LATEX, FontName.STANDARD,
			Schriftgroesse.NORMAL, layoutAntwortvorschlaege);
	}

	void checkPermission(final PersistenteRaetselgruppe ausDB) {

		if (!PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), ausDB.owner)) {

			LOGGER.warn("User {} hat versucht, Raetselgruppe {} mit Owner {} zu aendern oder zu drucken",
				authCtx.getUser().getName(), ausDB.uuid,
				ausDB.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}
	}
}
