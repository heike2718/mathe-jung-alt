// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;
import de.egladil.web.mja_auth.dto.MessagePayload;

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
	public Optional<RaetselgruppeDetails> loadDetails(final String raetselgruppeID) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			return Optional.empty();
		}

		final RaetselgruppeDetails result = RaetselgruppeDetails.createFromDB(raetselgruppe);

		List<PersistentesRaetselgruppenelement> elementeDB = raetselgruppenDao.loadElementeRaetselgruppe(raetselgruppeID);
		List<String> raetselIDs = elementeDB.stream().map(el -> el.raetselID).collect(Collectors.toList());
		List<PersistenteAufgabeReadonly> aufgaben = raetselgruppenDao.loadAufgabenByRaetselIds(raetselIDs);

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
	public RaetselgruppensucheTrefferItem raetselgruppeAnlegen(final EditRaetselgruppePayload payload, final String user) throws WebApplicationException {

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
		raetselgruppe.geaendertDurch = user;
		raetselgruppe.status = DomainEntityStatus.ERFASST;

		PersistenteRaetselgruppe persistierte = speichern(raetselgruppe);
		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		LOGGER.info("Rätselgruppe angelegt: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	@Override
	@Transactional
	public RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(final EditRaetselgruppePayload payload, final String user) throws WebApplicationException {

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

		mergeFromPayload(ausDB, payload);
		ausDB.geaendertDurch = user;

		PersistenteRaetselgruppe persistierte = speichern(ausDB);

		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = raetselgruppenDao.countElementeRaetselgruppe(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselgruppe geaendert: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	PersistenteRaetselgruppe speichern(final PersistenteRaetselgruppe raetselgruppe) {

		return raetselgruppenDao.saveRaetselgruppe(raetselgruppe);
	}

	boolean dupletteNachKeysExistiert(final EditRaetselgruppePayload payload) {

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
	public RaetselgruppeDetails elementAnlegen(final String raetselgruppeID, final EditRaetselgruppenelementPayload payload) {

		PersistenteRaetselgruppe raetselgruppe = raetselgruppenDao.findByID(raetselgruppeID);

		if (raetselgruppe == null) {

			Response response = Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.error("Tja, diese Rätselgruppe gibt es gar nicht."))
				.build();
			throw new WebApplicationException(response);
		}

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

	@Override
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

	@Override
	public RaetselgruppeDetails elementLoeschen(final String raetselgruppeID, final String elementID) {

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

}
