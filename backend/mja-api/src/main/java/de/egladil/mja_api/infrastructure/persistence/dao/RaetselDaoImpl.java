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

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.Suchmodus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * RaetselDaoImpl
 */
@ApplicationScoped
public class RaetselDaoImpl implements RaetselDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselDaoImpl.class);

	@ConfigProperty(name = "stage")
	String stage;

	@Inject
	EntityManager entityManager;

	@Override
	public PersistentesRaetsel getWithID(final String uuid) {

		return entityManager.find(PersistentesRaetsel.class, uuid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public long countRaetselVolltext(final String suchstring, final Suchmodus suchmodus, final boolean nurFreigegebene) {

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.concatVolltextWithSuchmodus(worte.length, suchmodus);

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE " + matcher;

		if (nurFreigegebene) {

			stmt = "SELECT count(*) FROM RAETSEL r WHERE " + matcher + " AND STATUS = :status";

		}

		// System.out.println(stmt);

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, Long.class);

		if (nurFreigegebene) {

			query
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString());
		}

		List<Long> trefferliste = query.getResultList();
		long anzahl = trefferliste.get(0).longValue();

		return anzahl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselVolltext(final String suchstring, final Suchmodus suchmodus, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.concatVolltextWithSuchmodus(worte.length, suchmodus);

		String stmt = "SELECT * FROM RAETSEL r WHERE " + matcher;

		if (nurFreigegebene) {

			stmt += " AND STATUS = :status ";
		}

		stmt += sortDirection == SortDirection.desc
			? " ORDER BY SCHLUESSEL desc"
			: " ORDER BY SCHLUESSEL";

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + "]");

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, PersistentesRaetsel.class);

		if (nurFreigegebene) {

			query
				.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString());
		}

		return query.getResultList();
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

	@Override
	@SuppressWarnings("unchecked")
	public long countRaetselWithFilter(final String suchstring, final String deskriptorenIDs, final Suchmodus suchmodus, final boolean nurFregegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.concatVolltextWithSuchmodus(worte.length, suchmodus)
			+ " AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		if (nurFregegebene) {

			matcher += " AND STATUS = :status ";
		}

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE " + matcher;

		// System.out.println(stmt);
		// System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, Long.class).setParameter("deskriptoren",
			wrappedDeskriptorenIds);

		if (nurFregegebene) {

			query.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString());
		}

		List<Long> trefferliste = query.getResultList();
		long anzahl = trefferliste.get(0).longValue();

		return anzahl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersistentesRaetsel> findRaetselWithFilter(final String suchstring, final String deskriptorenIDs, final Suchmodus suchmodus, final int limit, final int offset, final SortDirection sortDirection, final boolean nurFreigegebene) {

		String wrappedDeskriptorenIds = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		String[] worte = StringUtils.split(suchstring, ' ');

		String matcher = this.concatVolltextWithSuchmodus(worte.length, suchmodus)
			+ " AND CONCAT(CONCAT(',', DESKRIPTOREN),',') LIKE :deskriptoren";

		if (nurFreigegebene) {

			matcher += " AND STATUS = :status ";
		}

		String stmt = "select * from RAETSEL WHERE " + matcher;

		if (sortDirection == SortDirection.desc) {

			stmt += " ORDER BY SCHLUESSEL desc";
		} else {

			stmt += " ORDER BY SCHLUESSEL";
		}

		System.out.println(stmt);
		System.out.println("[suchstring=" + suchstring + ", deskriptoren=" + wrappedDeskriptorenIds + "]");

		Query query = this.createQueryAndReplaceSuchparameter(stmt, worte, PersistentesRaetsel.class).setParameter("deskriptoren",
			wrappedDeskriptorenIds);

		if (nurFreigegebene) {

			query.setParameter("status", DomainEntityStatus.FREIGEGEBEN.toString());
		}

		return query.getResultList();
	}

	@Override
	public long countRaetselWithStatus(final DomainEntityStatus status) {

		String stmt = "SELECT count(*) FROM RAETSEL r WHERE STATUS = :status";

		@SuppressWarnings("unchecked")
		List<Long> trefferliste = entityManager.createNativeQuery(stmt).setParameter("status", status.toString()).getResultList();

		long anzahl = trefferliste.get(0);

		return anzahl;
	}

	@Override
	public int getMaximalSchluessel() {

		String stmt = "SELECT max(r.schluessel) from RAETSEL r where SCHLUESSEL != :schluessel";

		@SuppressWarnings("unchecked")
		List<String> trefferliste = entityManager.createNativeQuery(stmt).setParameter("schluessel", "99999").getResultList();

		return Integer.valueOf(trefferliste.get(0)).intValue();
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

	/**
	 * @param  worte
	 * @param  suchmodus
	 * @return
	 */
	String concatVolltextWithSuchmodus(final int anzahlWorte, final Suchmodus suchmodus) {

		String part1 = "MATCH(SCHLUESSEL,NAME,KOMMENTAR,FRAGE,LOESUNG) AGAINST(:suchstring";

		List<String> parts = new ArrayList<>();

		for (int i = 0; i < anzahlWorte; i++) {

			String part = part1 + i + ")";

			parts.add(part);
		}

		return "(" + parts.stream().collect(Collectors.joining(suchmodus.getOperator())) + ")";
	}

	Query createQueryAndReplaceSuchparameter(final String stmt, final String[] worte, @SuppressWarnings("rawtypes") final Class clazz) {

		Query result = entityManager.createNativeQuery(stmt, clazz);

		for (int i = 0; i < worte.length; i++) {

			result.setParameter("suchstring" + i, worte[i]);
		}

		return result;
	}

}
