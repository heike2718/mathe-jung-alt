// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.SuchmodusVolltext;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesAufgabensammlungRaetselsucheItemReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * RaetselDao
 */
@Repository
@ApplicationScoped
public class RaetselDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselDao.class);

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	EntityManager entityManager;

	/**
	 * @param  uuid
	 *              String
	 * @return      PersistentesRaetel oder null
	 */
	public PersistentesRaetsel findById(final String uuid) {

		return entityManager.find(PersistentesRaetsel.class, uuid);
	}

	/**
	 * Persistiert ein neues RaetselHistorieItem
	 *
	 * @param neuesHistorieItem
	 *                          PersistentesRaetselHistorieItem
	 */
	public void insert(final PersistentesRaetselHistorieItem neuesHistorieItem) {

		entityManager.persist(neuesHistorieItem);

	}

	/**
	 * Speichert ein neues Rätsel oder ändert ein vorhandenes.
	 *
	 * @param persistentesRaetsel
	 */
	public void save(final PersistentesRaetsel persistentesRaetsel) {

		if (persistentesRaetsel.isPersistent()) {

			entityManager.merge(persistentesRaetsel);
		} else {

			entityManager.persist(persistentesRaetsel);
		}

	}

	/**
	 * Zählt alle RAETSEL, bei denen der Suchstring im Volltextindex enthalten ist. Dieser umfasst NAME, KOMMENTAR, FRAGE und
	 * LOESUNG. Bei mehreren Worten werden alle je nach suchmodus mit AND oder mit OR verknüpft.
	 *
	 * @param  suchstring
	 * @param  suchmodus
	 *                    SuchmodusVolltext mündet in AND oder OR, wenn der Suchstring aus mehr als einem Wort besteht.
	 * @return            List
	 */
	@SuppressWarnings("unchecked")
	public long countRaetselVolltext(final String suchstring, final SuchmodusVolltext suchmodus, final boolean nurFreigegebene) {

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.getVolltextMatcher(worte.length, suchmodus);

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE " + matcher;

		if (nurFreigegebene) {

			stmt = "SELECT count(*) FROM RAETSEL r WHERE " + matcher + " AND FREIGEGEBEN = :freigegeben";

		}

		// System.out.println(stmt);

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, Long.class);

		if (nurFreigegebene) {

			query
				.setParameter("freigegeben", true);
		}

		List<Long> trefferliste = query.getResultList();
		long anzahl = trefferliste.get(0).longValue();

		return anzahl;
	}

	/**
	 * Gibt alle RAETSEL innerhalb des Paginators zurück, bei denen der Suchstring im Volltextindex enthalten ist. Dieser umfasst
	 * NAME, KOMMENTAR, FRAGE und
	 * LOESUNG. Bei mehreren Worten werden alle je nach suchmodus mit AND oder mit OR verknüpft.
	 *
	 * @param  suchstring
	 * @param  suchmodus
	 *                       SuchmodusVolltext mündet in AND oder OR, wenn der Suchstring aus mehr als einem Wort besteht.
	 * @param  limit
	 *                       int Anzahl Treffer in page
	 * @param  offset
	 *                       int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                       SortDirection asc/desc
	 * @return               List
	 */
	@SuppressWarnings("unchecked")
	public List<PersistentesRaetsel> findRaetselVolltext(final String suchstring, final SuchmodusVolltext suchmodus, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.getVolltextMatcher(worte.length, suchmodus);

		String stmt = "SELECT * FROM RAETSEL r WHERE " + matcher;

		if (nurFreigegebene) {

			stmt += " AND FREIGEGEBEN = :freigegeben ";
		}

		stmt += sortDirection == SortDirection.desc
			? " ORDER BY SCHLUESSEL desc"
			: " ORDER BY SCHLUESSEL";

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + "]");

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, PersistentesRaetsel.class);

		if (nurFreigegebene) {

			query
				.setParameter("freigegeben", true);
		}

		return query.getResultList();
	}

	/**
	 * Zählt alle Raetsel, deren Deskriptoren die deskriptorenIDs als Teilmenge enthalten oder nicht enthalten.
	 *
	 * @param  deskriptorenIDs
	 *                               String die IDs der Deskriptoren
	 * @param  suchmodusDeskriptoren
	 *                               SuchmodusDeskriptoren
	 * @return                       List
	 */
	@SuppressWarnings("unchecked")
	public long countWithDeskriptoren(final String deskriptorenIDs, final SuchmodusDeskriptoren suchmodusDeskriptoren, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		LOGGER.debug("deskriptoren={}, suchmodusDeskriptoren={}, nurFreigegebene={}",
			wrappedDeskriptorenIds, suchmodusDeskriptoren, nurFreigegebene);

		String stmt = suchmodusDeskriptoren == SuchmodusDeskriptoren.LIKE
			? "SELECT count(*) FROM RAETSEL r WHERE CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren"
			: "SELECT count(*) FROM RAETSEL r WHERE CONCAT(CONCAT(',', DESKRIPTOREN),',') NOT LIKE :deskriptoren";

		if (nurFreigegebene) {

			stmt += " AND FREIGEGEBEN = :freigegeben";
		}

		// System.out.println(stmt);

		List<Long> trefferliste = null;

		if (nurFreigegebene) {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.setParameter("freigegeben", true)
				.getResultList();
		} else {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.getResultList();
		}

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	/**
	 * Sucht alle Raetsel, deren Deskriptoren die deskriptorenIDs als Teilmenge enthalten oder nicht enthalten.
	 *
	 * @param  deskriptorenIDs
	 *                               String die IDs der Deskriptoren
	 * @param  suchmodusDeskriptoren
	 *                               TODO
	 * @param  limit
	 *                               int Anzahl Treffer in page
	 * @param  offset
	 *                               int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                               SortDirection asc/desc
	 * @return                       List
	 */
	public List<PersistentesRaetsel> findWithDeskriptoren(final String deskriptorenIDs, final SuchmodusDeskriptoren suchmodusDeskriptoren, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String wrappedDeskriptoren = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		LOGGER.debug("deskriptoren={}, suchmodusDeskriptoren={}, nurFreigegebene={}",
			wrappedDeskriptoren, suchmodusDeskriptoren, nurFreigegebene);

		String queryName = queryNameFilteredSearch(suchmodusDeskriptoren, sortDirection, nurFreigegebene);

		LOGGER.debug("deskriptoren={}, suchmodus={}, limit={}, offset={}, queryName={}", wrappedDeskriptoren, suchmodusDeskriptoren,
			limit,
			offset, queryName);

		if (nurFreigegebene) {

			return entityManager.createNamedQuery(queryName, PersistentesRaetsel.class)
				.setParameter("deskriptoren", wrappedDeskriptoren)
				.setParameter("freigegeben", true)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
		}

		return entityManager.createNamedQuery(queryName, PersistentesRaetsel.class)
			.setParameter("deskriptoren", wrappedDeskriptoren)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	String queryNameFilteredSearch(final SuchmodusDeskriptoren suchmodusDeskriptoren, final SortDirection sortDirection, final boolean nurFreigegebene) {

		switch (suchmodusDeskriptoren) {

		case LIKE: {

			switch (sortDirection) {

			case asc: {

				return nurFreigegebene ? PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN
					: PersistentesRaetsel.FIND_WITH_DESKRIPTOREN;
			}

			case desc: {

				return nurFreigegebene ? PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC
					: PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC;
			}

			default:
				throw new IllegalArgumentException("Unexpected value: " + sortDirection);
			}
		}

		case NOT_LIKE: {

			switch (sortDirection) {

			case asc: {

				return nurFreigegebene ? PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN
					: PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN;
			}

			case desc: {

				return nurFreigegebene ? PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC
					: PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC;
			}

			default:
				throw new IllegalArgumentException("Unexpected value: " + sortDirection);
			}

		}

		default:
			throw new IllegalArgumentException("Unexpected value: " + suchmodusDeskriptoren);
		}

	}

	/**
	 * Zählt alle RAETSEL, bei denen die Volltextsuche Treffer ergibt (Worte werden entweder mit AND oder mit OR verknüpft) und mit
	 * Deskriptoren LIKE oder not LIKE. Der Volltextindex umfasst NAME, KOMMENTAR, FRAGE und LOESUNG.
	 *
	 * @param  suchfilter
	 *                         Suchfilter
	 * @param  nurFreigegebene
	 *                         boolean
	 * @return                 List
	 */
	@SuppressWarnings("unchecked")
	public long countRaetselWithFilter(final Suchfilter suchfilter, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(suchfilter.getDeskriptorenIds());

		LOGGER.debug("suchstring={}, deskriptoren={}, suchmodusVolltext={}, suchmodusDeskriptoren={}", suchfilter.getSuchstring(),
			wrappedDeskriptorenIds, suchfilter.getModusVolltext(), suchfilter.getModusDeskriptoren());

		String[] worte = StringUtils.split(suchfilter.getSuchstring(), ' ');

		String where = this.getVolltextMatcher(worte.length, suchfilter.getModusVolltext())
			+ " AND "
			+ getWhereDeskriptoren(suchfilter.getModusDeskriptoren());

		if (nurFreigegebene) {

			where += " AND FREIGEGEBEN = :freigegeben ";
		}

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE " + where;

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, Long.class).setParameter("deskriptoren",
			wrappedDeskriptorenIds);

		if (nurFreigegebene) {

			query.setParameter("freigegeben", true);
		}

		List<Long> trefferliste = query.getResultList();
		long anzahl = trefferliste.get(0).longValue();

		return anzahl;
	}

	/**
	 * Zählt alle RAETSEL, bei denen die Volltextsuche Treffer ergibt (mehrere Worte werden entweder mit AND oder mit OR verknüpft)
	 * und mit
	 * Deskriptoren LIKE oder not LIKE. Der Volltextindex umfasst NAME, KOMMENTAR, FRAGE und LOESUNG.
	 *
	 * @param  suchfilter
	 *                         Suchfilter
	 * @param  limit
	 *                         int Anzahl Treffer in page
	 * @param  offset
	 *                         int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                         SortDirection asc/desc
	 * @param  nurFreigegebene
	 *                         boolean
	 * @return                 List
	 */
	@SuppressWarnings("unchecked")
	public List<PersistentesRaetsel> findRaetselWithFilter(final Suchfilter suchfilter, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(suchfilter.getDeskriptorenIds());

		String[] worte = StringUtils.split(suchfilter.getSuchstring(), ' ');

		LOGGER.debug("suchstring={}, deskriptoren={}, suchmodusVolltext={}, suchmodusDeskriptoren={}", suchfilter.getSuchstring(),
			wrappedDeskriptorenIds, suchfilter.getModusVolltext(), suchfilter.getModusDeskriptoren());

		String where = this.getVolltextMatcher(worte.length, suchfilter.getModusVolltext())
			+ " AND "
			+ getWhereDeskriptoren(suchfilter.getModusDeskriptoren());

		if (nurFreigegebene) {

			where += " AND FREIGEGEBEN = :freigegeben ";
		}

		String stmt = "select * from RAETSEL WHERE " + where;

		if (sortDirection == SortDirection.desc) {

			stmt += " ORDER BY SCHLUESSEL desc";
		} else {

			stmt += " ORDER BY SCHLUESSEL";
		}

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, PersistentesRaetsel.class).setParameter("deskriptoren",
			wrappedDeskriptorenIds);

		if (nurFreigegebene) {

			query.setParameter("freigegeben", true);
		}

		return query.getResultList();
	}

	/**
	 * @param  status
	 * @return        long
	 */
	public long countRaetselWithStatus(final boolean freigegeben) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE FREIGEGEBEN = :freigegeben";

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = entityManager.createNativeQuery(stmt).setParameter("freigegeben", freigegeben).getResultList();

		long anzahl = trefferliste.get(0);

		return anzahl;
	}

	/**
	 * Gibt den maximalen in der DB existierenden RAETSEL.SCHLUESSEL zurück.
	 *
	 * @return String
	 */
	public int getMaximumOfAllSchluessel() {

		String stmt = "SELECT max(r.schluessel) from RAETSEL r where SCHLUESSEL != :schluessel";

		@SuppressWarnings("unchecked")
		List<String> trefferliste = entityManager.createNativeQuery(stmt).setParameter("schluessel", "99999").getResultList();

		return Integer.valueOf(trefferliste.get(0)).intValue();
	}

	/**
	 * @param  schluessel
	 * @return            PersistentesRaetsel oder null
	 */
	public PersistentesRaetsel findWithSchluessel(final String schluessel) {

		List<PersistentesRaetsel> trefferliste = entityManager
			.createNamedQuery(PersistentesRaetsel.FIND_WITH_SCHLUESSEL, PersistentesRaetsel.class)
			.setParameter("schluessel", schluessel).getResultList();

		if (trefferliste.size() > 1) {

			throw new MjaRuntimeException("Da ist in den Daten was faul: mehr als ein Raetsel mit schluessel=" + schluessel);
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	/**
	 * Selektiert alle Raetsel, deren SCHLUESSEL in der gegebenen Collection enthalten ist.<br>
	 * <br>
	 * <strong>Achtung:</strong> Es wird mit IN gesucht. Es gibt ein DB-Limit für die Länge der Liste. Das wird aktuell nicht
	 * berücksichtigt. Es dürfte momentan auch so groß sein, dass es nicht durch irgendwelche Aufgabensammlungen gerissen werden
	 * kann. Es
	 * gibt ein Issue diesbezüglich: https://github.com/heike2718/mathe-jung-alt/issues/105
	 *
	 * @param  schluessel
	 *                    List
	 * @return            List
	 */
	public List<PersistentesRaetsel> findWithSchluesselListe(final List<String> schluessel) {

		return entityManager.createNamedQuery(PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST, PersistentesRaetsel.class)
			.setParameter("schluessel", schluessel).getResultList();
	}

	/**
	 * @param  worte
	 * @param  suchmodus
	 * @return
	 */
	String getVolltextMatcher(final int anzahlWorte, final SuchmodusVolltext suchmodus) {

		String part1 = "MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring";

		List<String> parts = new ArrayList<>();

		for (int i = 0; i < anzahlWorte; i++) {

			String part = part1 + i + ")";

			parts.add(part);
		}

		return "(" + parts.stream().collect(Collectors.joining(suchmodus.getOperator())) + ")";
	}

	String getWhereDeskriptoren(final SuchmodusDeskriptoren suchmodusDeskriptoren) {

		switch (suchmodusDeskriptoren) {

		case LIKE: {

			return "CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";
		}

		case NOT_LIKE: {

			return "CONCAT(CONCAT(',', DESKRIPTOREN),',') NOT LIKE :deskriptoren";
		}

		default:
			throw new IllegalArgumentException("Unexpected value: " + suchmodusDeskriptoren);
		}
	}

	Query createQueryAndReplaceSuchparameter(final String stmt, final String[] worte, @SuppressWarnings("rawtypes") final Class clazz) {

		Query result = entityManager.createNativeQuery(stmt, clazz);

		for (int i = 0; i < worte.length; i++) {

			result.setParameter("suchstring" + i, worte[i]);
		}

		return result;
	}

	/**
	 * Sucht alle Aufgabensammlungen, in denen das gegebene Rätsel verwendet wird.
	 *
	 * @param  raetselId
	 *                   String die UUID des RAETSELS. Sortiert ist nach Name der Aufgabensammlungen.
	 * @return           List
	 */
	public List<PersistentesAufgabensammlungRaetselsucheItemReadonly> findAllAufgabensammlungenWithRaetsel(final String raetselId) {

		return entityManager
			.createNamedQuery(PersistentesAufgabensammlungRaetselsucheItemReadonly.FIND_WITH_RAETSEL_ID,
				PersistentesAufgabensammlungRaetselsucheItemReadonly.class)
			.setParameter("raetselId", raetselId)
			.getResultList();

	}
}
