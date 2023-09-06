// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * RaetselDaoImpl
 */
@ApplicationScoped
public class RaetselDaoImpl implements RaetselDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselDaoImpl.class);

	@Inject
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public long countRaetselVolltext(final String suchstring, final boolean nurFreigegebene) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring)";

		if (nurFreigegebene) {

			stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status";
		}

		List<Long> trefferliste = null;

		if (nurFreigegebene) {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("suchstring", suchstring)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString())
				.getResultList();
		} else {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("suchstring", suchstring)
				.getResultList();
		}

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long countWithDeskriptoren(final String deskriptorenIDs, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		if (nurFreigegebene) {

			stmt = "SELECT count(*) FROM RAETSEL r WHERE CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren AND STATUS = :status";
		}

		List<Long> trefferliste = null;

		if (nurFreigegebene) {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString())
				.getResultList();
		} else {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.getResultList();
		}

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long countRaetselWithFilter(final String suchstring, final String deskriptorenIDs, final boolean nurFregegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		if (nurFregegebene) {

			stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		}

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		List<Long> trefferliste = null;

		if (nurFregegebene) {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("suchstring", suchstring)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString())
				.getResultList();
		} else {

			trefferliste = entityManager.createNativeQuery(stmt)
				.setParameter("suchstring", suchstring)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.getResultList();
		}

		long anzahl = trefferliste.get(0);

		return anzahl;
	}

	@Override
	public long countRaetselWithStatus(final DomainEntityStatus status) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE STATUS = :status";

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = entityManager.createNativeQuery(stmt).setParameter("status", status.toString()).getResultList();

		long anzahl = trefferliste.get(0);

		return anzahl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselVolltext(final String suchstring, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL";

		if (nurFreigegebene) {

			stmt = sortDirection == SortDirection.desc
				? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status ORDER BY SCHLUESSEL desc"
				: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status ORDER BY SCHLUESSEL";
		}

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + "]");

		if (nurFreigegebene) {

			return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
				.setParameter("suchstring", suchstring)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString())
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
		}

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@Override
	public List<PersistentesRaetsel> findWithDeskriptoren(final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String wrappedDeskriptoren = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		LOGGER.debug("[deskriptoren=" + wrappedDeskriptoren + "]");

		String queryId = null;

		if (nurFreigegebene) {

			queryId = sortDirection == SortDirection.desc ? PersistentesRaetsel.FIND_WITH_STATUS_AND_DESKRIPTOREN_DESC
				: PersistentesRaetsel.FIND_WITH_STATUS_AND_DESKRIPTOREN;
		} else {

			queryId = sortDirection == SortDirection.desc ? PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC
				: PersistentesRaetsel.FIND_WITH_DESKRIPTOREN;
		}

		if (nurFreigegebene) {

			return entityManager.createNamedQuery(queryId, PersistentesRaetsel.class)
				.setParameter("deskriptoren", wrappedDeskriptoren)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
		}

		return entityManager.createNamedQuery(queryId, PersistentesRaetsel.class)
			.setParameter("deskriptoren", wrappedDeskriptoren)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselWithFilter(final String suchstring, final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL";

		if (nurFreigegebene) {

			stmt = sortDirection == SortDirection.desc
				? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL desc"
				: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND STATUS = :status AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL";
		}

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		if (nurFreigegebene) {

			return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
				.setParameter("suchstring", suchstring)
				.setParameter("deskriptoren", wrappedDeskriptorenIds)
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString())
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
		}

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@Override
	public PersistentesRaetsel findWithSchluessel(final String schluessel) {

		List<PersistentesRaetsel> trefferliste = entityManager
			.createNamedQuery(PersistentesRaetsel.FIND_WITH_SCHLUESSEL, PersistentesRaetsel.class)
			.setParameter("schluessel", schluessel).getResultList();

		if (trefferliste.size() > 1) {

			throw new MjaRuntimeException("Da ist in den Daten was faul: mehr als ein Raetsel mit schluessel=" + schluessel);
		}

		return trefferliste.isEmpty() ? null : trefferliste.get(0);
	}

	@Override
	public List<PersistentesRaetsel> findWithSchluessel(final List<String> schluessel) {

		return entityManager.createNamedQuery(PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST, PersistentesRaetsel.class)
			.setParameter("schluessel", schluessel).getResultList();
	}

}
