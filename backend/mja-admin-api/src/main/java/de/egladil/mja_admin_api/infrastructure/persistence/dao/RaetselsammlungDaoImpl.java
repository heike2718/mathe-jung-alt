// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;

import de.egladil.mja_admin_api.domain.DomainEntityStatus;
import de.egladil.mja_admin_api.domain.dto.SortDirection;
import de.egladil.mja_admin_api.domain.sammlungen.RaetselsammlungDao;
import de.egladil.mja_admin_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_admin_api.domain.sammlungen.Schwierigkeitsgrad;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteRaetselsammlung;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistentesRaetselsammlungselement;

/**
 * RaetselsammlungDaoImpl
 */
@ApplicationScoped
public class RaetselsammlungDaoImpl implements RaetselsammlungDao {

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

		return null;
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
	public List<PersistentesRaetselsammlungselement> findElementeZuRaetselsammlung(final String raetselsammlungID) {

		return entityManager
			.createNamedQuery(PersistentesRaetselsammlungselement.FIND_BY_SAMMLUNG, PersistentesRaetselsammlungselement.class)
			.setParameter("raetselsammlungID", raetselsammlungID).getResultList();
	}

}
