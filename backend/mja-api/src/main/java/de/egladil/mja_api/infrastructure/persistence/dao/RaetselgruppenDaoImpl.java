// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.math.BigInteger;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
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
	public long countByFilter(final RaetselgruppenSuchparameter suchparameter) {

		String stmt = "select count(*) from PersistenteRaetselgruppe g";
		String whereStmt = createWhereCondition(suchparameter);

		if (StringUtils.isNotBlank(whereStmt)) {

			stmt += " where " + whereStmt;
		}

		LOGGER.debug("stmt={}", stmt);

		Query query = entityManager.createQuery(stmt);

		if (StringUtils.isNotBlank(whereStmt)) {

			setParameters(query, suchparameter);
		}

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = query
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	public List<PersistenteRaetselgruppe> findByFilter(final RaetselgruppenSuchparameter suchparameter, final int limit, final int offset) {

		String stmt = "select g from PersistenteRaetselgruppe g";
		String whereStmt = createWhereCondition(suchparameter);

		if (StringUtils.isNotBlank(whereStmt)) {

			stmt += " where " + whereStmt;
		}

		// String sortStmt = createSortStatement(suchparameter);

		stmt += " order by g.name";

		TypedQuery<PersistenteRaetselgruppe> query = entityManager.createQuery(stmt,
			PersistenteRaetselgruppe.class).setFirstResult(offset)
			.setMaxResults(limit);

		setParameters(query, suchparameter);

		LOGGER.debug("stmt={}", stmt);

		return query.getResultList();
	}

	String createWhereCondition(final RaetselgruppenSuchparameter suchparameter) {

		String whereStmt = "";

		boolean addAnd = false;

		if (StringUtils.isNotBlank(suchparameter.name())) {

			whereStmt = " g.name like :name";
			addAnd = true;
		}

		if (suchparameter.schwierigkeitsgrad() != null) {

			if (addAnd) {

				whereStmt += " and g.schwierigkeitsgrad = :schwierigkeitsgrad";
			} else {

				whereStmt += " g.schwierigkeitsgrad = :schwierigkeitsgrad";
				addAnd = true;
			}

		}

		if (suchparameter.referenztyp() != null) {

			if (addAnd) {

				whereStmt += " and g.referenztyp = :referenztyp";
			} else {

				whereStmt += " g.referenztyp = :referenztyp";
				addAnd = true;
			}
		}

		if (StringUtils.isNotBlank(suchparameter.referenz())) {

			if (addAnd) {

				whereStmt += " and g.referenz like :referenz";
			} else {

				whereStmt += " g.referenz like :referenz";
			}
		}

		return whereStmt;
	}

	// String createSortStatement(final RaetselgruppenSuchparameter suchparameter) {
	//
	// String sortStmt = "";
	//
	// if (!suchparameter.hatSortierung()) {
	//
	// return " order by g.name asc";
	// }
	//
	// if (suchparameter.sortName().isDirection()) {
	//
	// sortStmt = " order by g.name " + suchparameter.sortName().toString();
	// } else {
	//
	// sortStmt = " order by g.name asc";
	// }
	//
	// if (suchparameter.sortReferenz().isDirection()) {
	//
	// sortStmt += ", g.referenz " + suchparameter.sortReferenz().toString();
	// }
	//
	// if (suchparameter.sortReferenztyp().isDirection()) {
	//
	// sortStmt += ", g.referenztyp " + suchparameter.sortReferenztyp().toString();
	// }
	//
	// if (suchparameter.sortSchwierigkeitsgrad().isDirection()) {
	//
	// sortStmt += ", g.schwierigkeitsgrad " + suchparameter.sortSchwierigkeitsgrad().toString();
	// }
	//
	// return sortStmt;
	// }

	void setParameters(final Query query, final RaetselgruppenSuchparameter suchparameter) {

		if (StringUtils.isNotBlank(suchparameter.name())) {

			query.setParameter("name", "%" + suchparameter.name().trim().toLowerCase() + "%");
		}

		if (suchparameter.schwierigkeitsgrad() != null) {

			query.setParameter("schwierigkeitsgrad", suchparameter.schwierigkeitsgrad());
		}

		if (suchparameter.referenztyp() != null) {

			query.setParameter("referenztyp", suchparameter.referenztyp());
		}

		if (StringUtils.isNotBlank(suchparameter.referenz())) {

			query.setParameter("referenz", "%" + suchparameter.referenz().trim().toLowerCase() + "%");
		}
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

	@Override
	public long countElementeRaetselgruppe(@NotNull @Size(min = 1, max = 40) final String uuid) {

		String stmt = "SELECT COUNT(*) from RAETSELGRUPPENELEMENTE e WHERE e.GRUPPE = :gruppe";

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt).setParameter("gruppe", uuid).getResultList();

		return trefferliste.get(0).longValue();
	}

}
