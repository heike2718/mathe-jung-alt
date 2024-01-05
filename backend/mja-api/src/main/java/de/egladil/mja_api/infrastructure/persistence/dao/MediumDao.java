// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselMediensucheItemReadonly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * MediumDao
 */
@Repository
@ApplicationScoped
public class MediumDao {

	@Inject
	EntityManager entityManager;

	/**
	 * Sucht das Medium anhand der technischen ID.
	 *
	 * @param  uuid
	 * @return      PersistentesMedium oder null
	 */
	public PersistentesMedium findMediumById(final String uuid) {

		return entityManager.find(PersistentesMedium.class, uuid);
	}

	/**
	 * Schreibt die Änderungen in die DB.
	 *
	 * @param  medium
	 * @return
	 */
	public PersistentesMedium saveMedium(final PersistentesMedium medium) {

		if (medium.isPersistent()) {

			return entityManager.merge(medium);
		}

		entityManager.persist(medium);

		return medium;
	}

	/**
	 * Gibt die maximale in der DB existierende QUELLEN.SORTNR zurück.
	 *
	 * @return int
	 */
	public int getMaximumOfAllSortNumbers() {

		List<Long> trefferliste = entityManager.createNamedQuery(PersistentesMedium.MAX_SORTNR, Long.class).getResultList();

		if (trefferliste.isEmpty()) {

			return 0;
		}

		return trefferliste.get(0) == null ? 0 : trefferliste.get(0).intValue();
	}

	/**
	 * Zählt alle Medien
	 *
	 * @param  suchstring
	 * @return            long
	 */
	public long countAllMedien() {

		List<Long> treffermenge = entityManager.createNamedQuery(PersistentesMedium.COUNT_ALL, Long.class).getResultList();

		return treffermenge.get(0);
	}

	/**
	 * Suche nach Medien, bei denen unabhängig von Groß- Kleinschreibung der titel oder der Kommentar den suchstring enthält.
	 *
	 * @param  suchstring
	 * @return            long
	 */
	public long countAllMedienWithSuchstring(final String suchstring) {

		String theSuchstring = "%" + suchstring + "%";

		List<Long> treffermenge = entityManager.createNamedQuery(PersistentesMedium.COUNT_WITH_SUCHSTRING, Long.class)
			.setParameter("suchstring", theSuchstring).getResultList();

		return treffermenge.get(0);
	}

	/**
	 * Zählt alle Medien mit dem Titel titel aber einer anderen UUID als die gegebene medienId. Falls > 0 würde das uk zuschlagen.
	 *
	 * @param  titel
	 *                  String es wird auf Gleichheit geprüft
	 * @param  mediumId
	 *                  es wird auf Ungleichheit geprüft
	 * @return          long
	 */
	public long countMedienWithSameTitel(final String titel, final String mediumId) {

		List<Long> treffermenge = entityManager.createNamedQuery(PersistentesMedium.ANZAHL_MIT_TITEL_GLEICH, Long.class)
			.setParameter("titel", titel)
			.setParameter("uuid", mediumId)
			.getResultList();

		return treffermenge.get(0);

	}

	/**
	 * Läd alle Medien.
	 *
	 * @param  suchstring
	 * @param  limit
	 * @param  offset
	 * @return            List
	 */
	public List<PersistentesMedium> loadAllMedien(final int limit, final int offset) {

		return entityManager.createNamedQuery(PersistentesMedium.LOAD_ALL, PersistentesMedium.class)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	/**
	 * Suche nach Medien, bei denen unabhängig von Groß- Kleinschreibung der titel oder der Kommentar den suchstring enthält.
	 * Sortiert wird nach titel.
	 *
	 * @param  suchstring
	 * @param  limit
	 * @param  offset
	 * @return            List
	 */
	public List<PersistentesMedium> findAllMedienWithSuchstring(final String suchstring, final int limit, final int offset) {

		String theSuchstring = "%" + suchstring + "%";

		return entityManager.createNamedQuery(PersistentesMedium.FIND_WITH_SUCHSTRING, PersistentesMedium.class)
			.setParameter("suchstring", theSuchstring)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	/**
	 * Gibt alle Medien der gegebenen Medienart sortiert nach titel zurück.
	 *
	 * @param  medienart
	 *                   Medienart
	 * @return           List
	 */
	public List<PersistentesMedium> findWithMedienart(final Medienart medienart) {

		return entityManager.createNamedQuery(PersistentesMedium.FIND_WITH_MEDIENART, PersistentesMedium.class)
			.setParameter("medienart", medienart)
			.getResultList();
	}

	/**
	 * Gibt alle Rätsel zurück, die das gegebene Medium als Quelle referenzieren.
	 *
	 * @param  mediumUuid
	 * @return            List
	 */
	public List<PersistentesRaetselMediensucheItemReadonly> findAllRaetselWithMedium(final String mediumUuid) {

		return entityManager
			.createNamedQuery(PersistentesRaetselMediensucheItemReadonly.FIND_WITH_MEDIUM_ID,
				PersistentesRaetselMediensucheItemReadonly.class)
			.setParameter("mediumUuid", mediumUuid)
			.getResultList();

	}
}
