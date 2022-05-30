// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.repositories;

import java.math.BigInteger;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.egladil.mathe_jung_alt_ws.domain.dto.SortDirection;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselDao;
import de.egladil.mathe_jung_alt_ws.domain.utils.SetOperationUtils;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselDaoImpl
 */
@ApplicationScoped
public class RaetselDaoImpl implements RaetselDao {

	@Inject
	EntityManager entityManager;

	@Override
	public long zaehleRaetselVolltext(final String suchstring) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring)";

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt)
			.setParameter("suchstring", suchstring)
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	public long zaehleMitDeskriptoren(final String deskriptorenIDs) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE DESKRIPTOREN LIKE :deskriptoren";

		@SuppressWarnings("unchecked")
		List<BigInteger> trefferliste = entityManager.createNativeQuery(stmt)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.getResultList();

		int anzahl = trefferliste.get(0).intValue();

		return anzahl;
	}

	@Override
	public long zaehleRaetselComplete(final String suchstring, final String deskriptorenIDs) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);
		String stmt = "SELECT count(*) FROM RAETSEL r WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND DESKRIPTOREN LIKE :deskriptoren";

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
	public List<PersistentesRaetsel> sucheRaetselVolltext(final String suchstring, final int limit, final int offset, final SortDirection sortDirection) {

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) ORDER BY SCHLUESSEL";

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@Override
	public List<PersistentesRaetsel> sucheMitDeskriptoren(final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection) {

		String suchstring = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String queryId = sortDirection == SortDirection.desc ? PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC
			: PersistentesRaetsel.FIND_WITH_DESKRIPTOREN;

		return entityManager.createNamedQuery(queryId, PersistentesRaetsel.class)
			.setParameter("deskriptoren", suchstring)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> sucheRaetselComplete(final String suchstring, final String deskriptorenIDs, final int limit, final int offset, final SortDirection sortDirection) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String stmt = sortDirection == SortDirection.desc
			? "select * from RAETSEL WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND DESKRIPTOREN LIKE :deskriptoren ORDER BY SCHLUESSEL desc"
			: "select * from RAETSEL WHERE MATCH(NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring) AND DESKRIPTOREN LIKE :deskriptoren ORDER BY SCHLUESSEL";

		return entityManager.createNativeQuery(stmt, PersistentesRaetsel.class)
			.setParameter("suchstring", suchstring)
			.setParameter("deskriptoren", wrappedDeskriptorenIds)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}
}
