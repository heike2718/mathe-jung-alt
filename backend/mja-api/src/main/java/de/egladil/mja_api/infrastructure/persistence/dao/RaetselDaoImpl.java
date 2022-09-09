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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselDaoImpl
 */
@ApplicationScoped
public class RaetselDaoImpl implements RaetselDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselDaoImpl.class);

	@Inject
	EntityManager entityManager;

	@Override
	public long countRaetselVolltext(final String suchstring) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring)";

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt)
			.setParameter("suchstring", suchstring)
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	public long countWithDeskriptoren(final String deskriptorenIDs) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	public long countRaetselWithFilter(final String suchstring, final String deskriptorenIDs) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt)
			.setParameter("suchstring", suchstring)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselVolltext(final String suchstring, final int limit, final int offset, final SortDirection sortDirection) {

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL";

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + "]");

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@Override
	public List<PersistentesRaetsel> findWithDeskriptoren(final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection) {

		String wrappedDeskriptoren = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		LOGGER.debug("[deskriptoren=" + wrappedDeskriptoren + "]");

		String queryId = sortDirection == SortDirection.desc ? PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC
			: PersistentesRaetsel.FIND_WITH_DESKRIPTOREN;

		return entityManager.createNamedQuery(queryId, PersistentesRaetsel.class)
			.setParameter("deskriptoren", wrappedDeskriptoren)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselWithFilter(final String suchstring, final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren ORDER BY SCHLUESSEL";

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}
}
