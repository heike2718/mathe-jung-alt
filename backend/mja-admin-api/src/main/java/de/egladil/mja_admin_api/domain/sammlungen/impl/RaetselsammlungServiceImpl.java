// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.AbstractDomainEntity;
import de.egladil.mja_admin_api.domain.DomainEntityStatus;
import de.egladil.mja_admin_api.domain.sammlungen.RaetselsammlungDao;
import de.egladil.mja_admin_api.domain.sammlungen.RaetselsammlungService;
import de.egladil.mja_admin_api.domain.sammlungen.dto.EditRaetselsammlungPayload;
import de.egladil.mja_admin_api.domain.sammlungen.dto.RaetselsammlungSucheTrefferItem;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteRaetselsammlung;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * RaetselsammlungServiceImpl
 */
@ApplicationScoped
public class RaetselsammlungServiceImpl implements RaetselsammlungService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselsammlungServiceImpl.class);

	@Inject
	RaetselsammlungDao raetselsammlungDao;

	/**
	 * Erstellt eine neue Rätselsammlung. Dabei wird geprüft, ob es eine mit den Keys bereits gibt. In diesem Fall wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselSucheTrefferItem
	 */
	@Override
	public RaetselsammlungSucheTrefferItem raetselsammlungAnlegen(final EditRaetselsammlungPayload payload, final String user) {

		if (!AbstractDomainEntity.UUID_NEUE_ENTITY.equals(payload.getId())) {

			LOGGER.error("POST mit vorhandener Entity uuid={} aufgerufen", payload.getId());

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("POST darf nur mit id='neu' aufgerufen werden"))
					.build());
		}

		if (dupletteNachKeysExistiert(payload)) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselsammlung mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselsammlung mit diesem Namen."))
					.build());
		}

		PersistenteRaetselsammlung raetselsammlung = mapFromPayload(payload);
		raetselsammlung.geaendertDurch = user;
		raetselsammlung.status = DomainEntityStatus.ERFASST;

		PersistenteRaetselsammlung persistierte = speichern(raetselsammlung);
		RaetselsammlungSucheTrefferItem result = mapFromDB(persistierte);

		LOGGER.info("Rätselsammlung angelegt: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	@Override
	public RaetselsammlungSucheTrefferItem raetselsammlungBasisdatenAendern(final EditRaetselsammlungPayload payload, final String user) {

		if (dupletteNachKeysExistiert(payload)) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselsammlung mit der gleichen Referenz."))
					.build());
		}

		if (namensdubletteExistiert(payload.getName(), payload.getId())) {

			throw new WebApplicationException(
				Response.status(Status.CONFLICT)
					.entity(MessagePayload.error("Es gibt bereits eine Rätselsammlung mit diesem Namen."))
					.build());
		}

		PersistenteRaetselsammlung raetselsammlung = mapFromPayload(payload);
		raetselsammlung.geaendertDurch = user;

		PersistenteRaetselsammlung persistierte = speichern(raetselsammlung);

		RaetselsammlungSucheTrefferItem result = mapFromDB(persistierte);

		long anzahlElemente = raetselsammlungDao.countElementeZuRaetselsammlung(persistierte.uuid);
		result.setAnzahlElemente(anzahlElemente);

		LOGGER.info("Raetselsammlung geaendert: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return result;
	}

	@Transactional
	PersistenteRaetselsammlung speichern(final PersistenteRaetselsammlung raetselsammlung) {

		return raetselsammlungDao.saveRaetselsammlung(raetselsammlung);
	}

	boolean dupletteNachKeysExistiert(final EditRaetselsammlungPayload payload) {

		PersistenteRaetselsammlung persistente = raetselsammlungDao.findByUniqueKey(payload.getReferenztyp(),
			payload.getReferenz(), payload.getSchwierigkeitsgrad());

		if (persistente == null) {

			return false;
		}

		return !persistente.uuid.equals(payload.getId());

	}

	boolean namensdubletteExistiert(final String name, final String uuid) {

		PersistenteRaetselsammlung persistente = raetselsammlungDao.findByName(name);

		if (persistente == null) {

			return false;
		}

		return !uuid.equals(persistente.uuid);
	}

	RaetselsammlungSucheTrefferItem mapFromDB(final PersistenteRaetselsammlung ausDB) {

		RaetselsammlungSucheTrefferItem result = new RaetselsammlungSucheTrefferItem();
		result.setId(ausDB.uuid);
		result.setKommentar(ausDB.kommentar);
		result.setName(ausDB.name);
		result.setReferenz(ausDB.referenz);
		result.setReferenztyp(ausDB.referenztyp);
		result.setSchwierigkeitsgrad(ausDB.schwierigkeitsgrad);
		result.setStatus(ausDB.status);

		return result;

	}

	PersistenteRaetselsammlung mapFromPayload(final EditRaetselsammlungPayload payload) {

		PersistenteRaetselsammlung raetselsammlung = new PersistenteRaetselsammlung();
		raetselsammlung.kommentar = payload.getKommentar();
		raetselsammlung.name = payload.getName();
		raetselsammlung.referenz = payload.getReferenz();
		raetselsammlung.referenztyp = payload.getReferenztyp();
		raetselsammlung.schwierigkeitsgrad = payload.getSchwierigkeitsgrad();
		raetselsammlung.status = DomainEntityStatus.ERFASST;

		return raetselsammlung;
	}
}
