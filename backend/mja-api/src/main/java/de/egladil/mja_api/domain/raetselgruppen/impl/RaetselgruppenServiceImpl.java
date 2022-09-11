// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.impl;

import java.util.List;

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
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * RaetselgruppenServiceImpl
 */
@ApplicationScoped
public class RaetselgruppenServiceImpl implements RaetselgruppenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppenServiceImpl.class);

	@Inject
	RaetselgruppenDao raetselgruppenDao;

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

		PersistenteRaetselgruppe raetselgruppe = mapFromPayload(payload);
		raetselgruppe.geaendertDurch = user;
		raetselgruppe.status = DomainEntityStatus.ERFASST;

		PersistenteRaetselgruppe persistierte = speichern(raetselgruppe);
		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		LOGGER.info("Rätselgruppe angelegt: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	@Override
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

		PersistenteRaetselgruppe raetselgruppe = mapFromPayload(payload);
		raetselgruppe.geaendertDurch = user;

		PersistenteRaetselgruppe persistierte = speichern(raetselgruppe);

		RaetselgruppensucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = raetselgruppenDao.countElementeRaetselgruppe(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselgruppe geaendert: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	@Transactional
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

		return result;

	}

	PersistenteRaetselgruppe mapFromPayload(final EditRaetselgruppePayload payload) {

		PersistenteRaetselgruppe raetselgruppe = new PersistenteRaetselgruppe();
		raetselgruppe.kommentar = payload.getKommentar();
		raetselgruppe.name = payload.getName();
		raetselgruppe.referenz = payload.getReferenz();
		raetselgruppe.referenztyp = payload.getReferenztyp();
		raetselgruppe.schwierigkeitsgrad = payload.getSchwierigkeitsgrad();
		raetselgruppe.status = DomainEntityStatus.ERFASST;

		return raetselgruppe;
	}
}
