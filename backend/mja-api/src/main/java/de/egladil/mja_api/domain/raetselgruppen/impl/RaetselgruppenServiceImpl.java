// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.impl;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselgruppeGeneratorService;
import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

/**
 * RaetselgruppenServiceImpl
 */
@ApplicationScoped
public class RaetselgruppenServiceImpl implements RaetselgruppenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppenServiceImpl.class);

	@Inject
	RaetselgruppenDao raetselgruppenDao;

	@Inject
	RaetselService raetselService;

	@Inject
	QuizService quizService;

	@Inject
	RaetselgruppeGeneratorService raetselgruppeFileService;

	@Override
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

	@Override
	public Optional<RaetselgruppeDetails> loadDetails(final String raetselgruppeID, final AuthenticatedUser user) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			return Optional.empty();
		}

		final RaetselgruppeDetails result = RaetselgruppeDetails.createFromDB(raetselgruppe);

		if (PermissionUtils.hasWritePermission(user, raetselgruppe.owner)) {

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

	@Override
	@Transactional
	public RaetselgruppensucheTrefferItem raetselgruppeAnlegen(final EditRaetselgruppePayload payload, final AuthenticatedUser user) throws WebApplicationException {

		if (user == null) {

			LOGGER.error("an dieser Stelle darf der user nicht null sein!!!");
			throw new MjaRuntimeException("AuthenticatedUser ist null");
		}

		String userId = user.getUuid();

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

		LOGGER.info("Rätselgruppe angelegt: {}, user={}", result.getId(), StringUtils.abbreviate(userId, 11));

		return result;
	}

	@Override
	@Transactional
	public RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(final EditRaetselgruppePayload payload, final AuthenticatedUser user) throws WebApplicationException {

		if (user == null) {

			LOGGER.error("an dieser Stelle darf der user nicht null sein!!!");
			throw new MjaRuntimeException("AuthenticatedUser ist null");
		}

		String userId = user.getUuid();

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

		checkPermission(ausDB, user);

		mergeFromPayload(ausDB, payload);
		ausDB.geaendertDurch = userId;

		PersistenteRaetselgruppe persistierte = speichern(ausDB);

		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = raetselgruppenDao.countElementeRaetselgruppe(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselgruppe geaendert: {}, user={}", result.getId(), StringUtils.abbreviate(userId, 11));

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

	@Override
	public RaetselgruppeDetails elementAnlegen(final String raetselgruppeID, final EditRaetselgruppenelementPayload payload, final AuthenticatedUser user) {

		if (user == null) {

			LOGGER.error("an dieser Stelle darf der user nicht null sein!!!");
			throw new MjaRuntimeException("AuthenticatedUser ist null");
		}

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

		checkPermission(raetselgruppe, user);

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

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID, user);

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

	@Override
	@Transactional
	public RaetselgruppeDetails elementAendern(final String raetselgruppeID, final EditRaetselgruppenelementPayload payload, final AuthenticatedUser user) {

		if (user == null) {

			LOGGER.error("an dieser Stelle darf der user nicht null sein!!!");
			throw new MjaRuntimeException("AuthenticatedUser ist null");
		}

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

		checkPermission(raetselgruppe, user);

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

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID, user);

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

	@Override
	public RaetselgruppeDetails elementLoeschen(final String raetselgruppeID, final String elementID, final AuthenticatedUser user) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe != null) {

			checkPermission(raetselgruppe, user);
		}

		raetselgruppenDao.deleteRaetselgruppenelement(elementID);

		Optional<RaetselgruppeDetails> opt = this.loadDetails(raetselgruppeID, user);

		if (opt.isEmpty()) {

			LOGGER.error("Raetselgruppe mit der UUID={} gibt es nicht", raetselgruppeID);
			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Ups, da ist aber etwas komplett schiefgelaufen"))
				.build();
			throw new WebApplicationException(response);
		}

		return opt.get();
	}

	@Override
	public GeneratedFile printVorschau(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		return raetselgruppeFileService.generateVorschauPDFQuiz(dbResult, aufgaben, layoutAntwortvorschlaege);
	}

	@Override
	public GeneratedFile downloadLaTeXSource(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = this.quizService.getItemsAsQuizaufgaben(raetselgruppeID);

		return raetselgruppeFileService.downloadLaTeXSource(dbResult, aufgaben, layoutAntwortvorschlaege);
	}

	void checkPermission(final PersistenteRaetselgruppe ausDB, final AuthenticatedUser user) {

		if (!PermissionUtils.hasWritePermission(user, ausDB.owner)) {

			LOGGER.warn("User {} hat versucht, Raetselgruppe {} mit Owner {} zu aendern", user.getUuid(), ausDB.uuid,
				ausDB.owner);

			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
	}

}
