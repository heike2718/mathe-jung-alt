package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelle;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@Repository
@ApplicationScoped
public class QuellenRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuellenRepository.class);

	@Inject
	EntityManager entityManager;

	/**
	 * Exakte Suche nach person.
	 *
	 * @param  name
	 *              String
	 * @return      Optional
	 */
	public Optional<PersistenteQuelleReadonly> findQuelleWithUserId(final String userId) {

		List<PersistenteQuelleReadonly> trefferliste = entityManager
			.createNamedQuery(PersistenteQuelleReadonly.FIND_WITH_USER_ID, PersistenteQuelleReadonly.class)
			.setParameter("userId", userId).getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.warn("Es gibt mehr als eine Quelle mit userId={} - es wird die erste zurückgegeben", userId);
		}

		return trefferliste.isEmpty() ? Optional.empty() : Optional.of(trefferliste.get(0));
	}

	/**
	 * Diese Methode gibt die um ein Medium angereicherten Daten einer Quelle zurück.
	 *
	 * @param  id
	 *            String die id
	 * @return    PersistenteQuelleReadonly oder null
	 */
	public PersistenteQuelleReadonly findQuelleReadonlyById(final String id) {

		return entityManager.find(PersistenteQuelleReadonly.class, id);
	}

	/**
	 * Diese Methode gibt die Entity zurück, die geändert werden kann.
	 *
	 * @param  id
	 *            String technische ID
	 * @return    PersistenteQuelle oder null
	 */
	public PersistenteQuelle findQuelleEntityWithId(final String id) {

		return entityManager.find(PersistenteQuelle.class, id);

	}
}
