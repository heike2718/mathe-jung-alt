// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;

/**
 * RaetselgruppenDaoImpl
 */
@ApplicationScoped
public class RaetselgruppenDaoImpl implements RaetselgruppenDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppenDaoImpl.class);

	@Inject
	EntityManager entityManager;

	@Override
	public long countByFilter(final String name, final String kommentar, final Schwierigkeitsgrad schwierigkeitsgrad, final Referenztyp referenztyp, final String referenz, final DomainEntityStatus status) {

		return 0;
	}

	@Override
	public List<PersistenteRaetselgruppe> findByFilter(final String name, final String kommentar, final Schwierigkeitsgrad schwierigkeitsgrad, final Referenztyp referenztyp, final String referenz, final DomainEntityStatus status, final int limit, final int offset, final SortDirection sortDirection) {

		return null;
	}

	@Override
	public PersistenteRaetselgruppe findByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		List<PersistenteRaetselgruppe> trefferliste = entityManager
			.createNamedQuery(PersistenteRaetselgruppe.FIND_BY_UNIQUE_KEY, PersistenteRaetselgruppe.class)
			.setParameter("schwierigkeitsgrad", schwierigkeitsgrad)
			.setParameter("referenztyp", referenztyp)
			.setParameter("referenz", referenz)
			.getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.error("{} Treffer zu einem eigentlich eindeutigen key referenzty={}, referenz={}, schwierigkeitsgrad={}",
				trefferliste.size(), referenztyp, referenz, schwierigkeitsgrad);
			throw new MjaRuntimeException("mehr als 1 Treffer zu einem eigentlich eindeutigen key");
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	@Override
	public PersistenteRaetselgruppe findByName(final String name) {

		List<PersistenteRaetselgruppe> trefferliste = entityManager
			.createNamedQuery(PersistenteRaetselgruppe.FIND_BY_NAME, PersistenteRaetselgruppe.class).setParameter("name", name)
			.getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.error("{} Treffer mit dem Namen name={}",
				trefferliste.size(), name);
			throw new MjaRuntimeException("mehr als 1 Treffer mit diesem Namen");
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	@Override
	public List<PersistenteAufgabeReadonly> loadAufgabenByRaetselIds(final List<String> uuids) {

		Session session = entityManager.unwrap(Session.class);

		MultiIdentifierLoadAccess<PersistenteAufgabeReadonly> multiLoadAccess = session
			.byMultipleIds(PersistenteAufgabeReadonly.class);
		List<PersistenteAufgabeReadonly> trefferliste = multiLoadAccess.multiLoad(uuids);

		return trefferliste;
	}

	@Override
	public long countElementeRaetselgruppe(final String gruppeID) {

		return 0;
	}

	@Override
	public List<PersistentesRaetselgruppenelement> loadElementeRaetselgruppe(final String gruppeID) {

		return entityManager
			.createNamedQuery(PersistentesRaetselgruppenelement.LOAD_BY_GRUPPE, PersistentesRaetselgruppenelement.class)
			.setParameter("raetselgruppeID", gruppeID).getResultList();
	}

	@Override
	public PersistenteRaetselgruppe saveRaetselgruppe(final PersistenteRaetselgruppe gruppe) {

		if (gruppe.isPersistent()) {

			return entityManager.merge(gruppe);
		}

		entityManager.persist(gruppe);

		return gruppe;
	}

}
