package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.semantik.Repository;
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
	 * Unscharfe Suche nach Quellen zu einem gegeben Medium oder einer Person. Gesucht wird mit like.
	 *
	 * @param  suchstring
	 *                    String
	 * @return            List
	 */
	public List<PersistenteQuelleReadonly> findQuellenLikeMediumOrPerson(final String suchstring) {

		String text = "%" + suchstring.toLowerCase() + "%";

		return entityManager.createNamedQuery(PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON, PersistenteQuelleReadonly.class)
			.setParameter("suchstring", text).getResultList();
	}

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
	 * @param  id
	 *            String die id
	 * @return    Optional
	 */
	public Optional<PersistenteQuelleReadonly> findById(final String id) {

		PersistenteQuelleReadonly result = entityManager.find(PersistenteQuelleReadonly.class, id);

		return result == null ? Optional.empty() : Optional.of(result);
	}
}
