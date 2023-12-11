// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.aufgabensammlungen.AufgabensammlungenSuchparameter;
import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesAufgabensammlugnselement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * AufgabensammlungDao
 */
@Repository
@ApplicationScoped
public class AufgabensammlungDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabensammlungDao.class);

	@Inject
	EntityManager entityManager;

	/**
	 * Zählt die Treffermenge bei Anwendung des gegebenen Filters. Die Parameter können null sein. Bei name und kommentar wird mit
	 * like gesucht, alle anderen mit equal.
	 *
	 * @param  suchparameter
	 * @return               long
	 */
	public long countByFilter(final AufgabensammlungenSuchparameter suchparameter) {

		String stmt = "select count(*) from PersistenteAufgabensammlung g";
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

	/**
	 * Sucht die Rätselgruppen die den Filterkriterien entsprechen, sortiert nach name. Die Parameter können null sein. Bei name
	 * und kommentar wird mit like gesucht, alle anderen mit equal.
	 *
	 * @param  suchparameter
	 * @param  limit
	 * @param  offset
	 * @return               List
	 */
	public List<PersistenteAufgabensammlung> findByFilter(final AufgabensammlungenSuchparameter suchparameter, final int limit, final int offset) {

		String stmt = "select g from PersistenteAufgabensammlung g";
		String whereStmt = createWhereCondition(suchparameter);

		if (StringUtils.isNotBlank(whereStmt)) {

			stmt += " where " + whereStmt;
		}

		String sortStmt = " order by g." + suchparameter.sortAttribute().toString() + " "
			+ suchparameter.sortDirection().toString();

		stmt += sortStmt;

		TypedQuery<PersistenteAufgabensammlung> query = entityManager.createQuery(stmt,
			PersistenteAufgabensammlung.class).setFirstResult(offset)
			.setMaxResults(limit);

		setParameters(query, suchparameter);

		LOGGER.debug("stmt={}", stmt);

		return query.getResultList();
	}

	String createWhereCondition(final AufgabensammlungenSuchparameter suchparameter) {

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

	void setParameters(final Query query, final AufgabensammlungenSuchparameter suchparameter) {

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

	/**
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    PersistenteAufgabensammlung oder null
	 */
	public PersistenteAufgabensammlung findByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		LOGGER.debug(" ==> (2)");
		List<PersistenteAufgabensammlung> trefferliste = entityManager
			.createNamedQuery(PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY, PersistenteAufgabensammlung.class)
			.setParameter("schwierigkeitsgrad", schwierigkeitsgrad)
			.setParameter("referenztyp", referenztyp)
			.setParameter("referenz", referenz)
			.getResultList();

		LOGGER.debug(" ==> (3)");

		if (trefferliste.size() > 1) {

			LOGGER.error("{} Treffer zu einem eigentlich eindeutigen key referenzty={}, referenz={}, schwierigkeitsgrad={}",
				trefferliste.size(), referenztyp, referenz, schwierigkeitsgrad);
			throw new MjaRuntimeException("mehr als 1 Treffer zu einem eigentlich eindeutigen key");
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	/**
	 * @param  aufgabensammlungID
	 * @return
	 */
	public PersistenteAufgabensammlung findByID(final String raetselgruppeID) {

		return entityManager.find(PersistenteAufgabensammlung.class, raetselgruppeID);
	}

	/**
	 * @param  name
	 * @return      PersistenteAufgabensammlung oder null
	 */
	public PersistenteAufgabensammlung findByName(final String name) {

		List<PersistenteAufgabensammlung> trefferliste = entityManager
			.createNamedQuery(PersistenteAufgabensammlung.FIND_BY_NAME, PersistenteAufgabensammlung.class)
			.setParameter("name", name)
			.getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.error("{} Treffer mit dem Namen name={}",
				trefferliste.size(), name);
			throw new MjaRuntimeException("mehr als 1 Treffer mit diesem Namen");
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	/**
	 * Gibt alle Elemente der gegebenen Rästelgruppe zurück.
	 *
	 * @param  gruppeID
	 * @return          List
	 */
	public List<PersistentesAufgabensammlugnselement> loadElementeRaetselgruppe(final String gruppeID) {

		return entityManager
			.createNamedQuery(PersistentesAufgabensammlugnselement.LOAD_BY_AUFGABENSAMMLUNG,
				PersistentesAufgabensammlugnselement.class)
			.setParameter("aufgabensammlungID", gruppeID).getResultList();
	}

	/**
	 * Läd alle Aufgaben der gegebenen Rätselgruppe.
	 *
	 * @param  raetselgruppeId
	 * @return                 List
	 */
	public List<PersistenteAufgabeReadonly> loadAufgabenByReaetselgruppe(final String raetselgruppeId) {

		return entityManager
			.createNamedQuery(PersistenteAufgabeReadonly.LOAD_AUFGABEN_IN_SAMMLUNG, PersistenteAufgabeReadonly.class)
			.setParameter("sammlung", raetselgruppeId).getResultList();
	}

	/**
	 * Speichert die gegebene Rätselgruppe und gibt die gespeicherte Entity zurück.
	 *
	 * @param  sammlung
	 *                  PersistenteAufgabensammlung
	 * @return          PersistenteAufgabensammlung
	 */
	public PersistenteAufgabensammlung saveRaetselgruppe(final PersistenteAufgabensammlung gruppe) {

		if (gruppe.isPersistent()) {

			return entityManager.merge(gruppe);
		}

		entityManager.persist(gruppe);

		return gruppe;
	}

	/**
	 * @param  uuid
	 * @return
	 */
	public long countElementeRaetselgruppe(@NotNull @Size(min = 1, max = 40) final String uuid) {

		String stmt = "SELECT COUNT(*) from AUFGABENSAMMLUNGSELEMENTE e WHERE e.SAMMLUNG = :sammlung";

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = entityManager.createNativeQuery(stmt).setParameter("sammlung", uuid).getResultList();

		return trefferliste.get(0).longValue();
	}

	/**
	 * @param  raetselgruppenelementID
	 * @return                         PersistentesAufgabensammlugnselement
	 */
	public PersistentesAufgabensammlugnselement findElementById(final String raetselgruppenelementID) {

		return entityManager.find(PersistentesAufgabensammlugnselement.class, raetselgruppenelementID);
	}

	/**
	 * @param  element
	 * @return         PersistentesAufgabensammlugnselement
	 */
	public PersistentesAufgabensammlugnselement saveRaetselgruppenelement(final PersistentesAufgabensammlugnselement element) {

		if (element.isPersistent()) {

			return entityManager.merge(element);
		}

		entityManager.persist(element);
		return element;
	}

	/**
	 * @param element
	 */
	@Transactional
	public void deleteRaetselgruppenelement(final String elementID) {

		final PersistentesAufgabensammlugnselement element = entityManager.find(PersistentesAufgabensammlugnselement.class,
			elementID);

		if (element != null) {

			entityManager.remove(element);
		}
	}
}
