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
import de.egladil.mja_api.domain.sammlungen.RaetselsammlungDao;
import de.egladil.mja_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_api.domain.sammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselsammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselsammlungselement;

/**
 * RaetselsammlungDaoImpl
 */
@ApplicationScoped
public class RaetselsammlungDaoImpl implements RaetselsammlungDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselsammlungDaoImpl.class);

	@Inject
	EntityManager entityManager;

	@Override
	public long countByFilter(final String name, final String kommentar, final Schwierigkeitsgrad schwierigkeitsgrad, final Referenztyp referenztyp, final String referenz, final DomainEntityStatus status) {

		return 0;
	}

	@Override
	public List<PersistenteRaetselsammlung> findByFilter(final String name, final String kommentar, final Schwierigkeitsgrad schwierigkeitsgrad, final Referenztyp referenztyp, final String referenz, final DomainEntityStatus status, final int limit, final int offset, final SortDirection sortDirection) {

		return null;
	}

	@Override
	public PersistenteRaetselsammlung findByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		List<PersistenteRaetselsammlung> trefferliste = entityManager
			.createNamedQuery(PersistenteRaetselsammlung.FIND_BY_UNIQUE_KEY, PersistenteRaetselsammlung.class)
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
	public PersistenteRaetselsammlung findByName(final String name) {

		List<PersistenteRaetselsammlung> trefferliste = entityManager
			.createNamedQuery(PersistenteRaetselsammlung.FIND_BY_NAME, PersistenteRaetselsammlung.class).setParameter("name", name)
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
	public long countElementeZuRaetselsammlung(final String raetselsammlungID) {

		return 0;
	}

	@Override
	public List<PersistentesRaetselsammlungselement> loadElementeZuRaetselsammlung(final String raetselsammlungID) {

		return entityManager
			.createNamedQuery(PersistentesRaetselsammlungselement.LOAD_BY_SAMMLUNG, PersistentesRaetselsammlungselement.class)
			.setParameter("raetselsammlungID", raetselsammlungID).getResultList();
	}

	@Override
	public PersistenteRaetselsammlung saveRaetselsammlung(final PersistenteRaetselsammlung sammlung) {

		if (sammlung.isPersistent()) {

			return entityManager.merge(sammlung);
		}

		entityManager.persist(sammlung);

		return sammlung;
	}

}
