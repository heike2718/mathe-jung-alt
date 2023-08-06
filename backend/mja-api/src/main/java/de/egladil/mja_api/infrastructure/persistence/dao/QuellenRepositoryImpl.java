package de.egladil.mja_api.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.quellen.QuellenRepository;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

@ApplicationScoped
public class QuellenRepositoryImpl implements QuellenRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuellenRepositoryImpl.class);

	@Inject
	EntityManager entityManager;

	@Override
	public List<PersistenteQuelleReadonly> findQuellenLikeMediumOrPerson(final String suchstring) {

		String text = "%" + suchstring.toLowerCase() + "%";

		return entityManager.createNamedQuery(PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON, PersistenteQuelleReadonly.class)
			.setParameter("suchstring", text).getResultList();
	}

	@Override
	public Optional<PersistenteQuelleReadonly> findQuelleWithUserId(final String userId) {

		List<PersistenteQuelleReadonly> trefferliste = entityManager
			.createNamedQuery(PersistenteQuelleReadonly.FIND_WITH_USER_ID, PersistenteQuelleReadonly.class)
			.setParameter("userId", userId).getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.warn("Es gibt mehr als eine Quelle mit userId={} - es wird die erste zurückgegeben", userId);
		}

		return trefferliste.isEmpty() ? Optional.empty() : Optional.of(trefferliste.get(0));
	}

	@Override
	public List<PersistenteQuelleReadonly> findWithDeskriptoren(final String deskriptorenIDs) {

		String wrappedDeskriptoren = new SetOperationUtils().prepareForDeskriptorenLikeSearch(deskriptorenIDs);

		LOGGER.info("[deskriptoren=" + wrappedDeskriptoren + "]");

		String queryId = PersistenteQuelleReadonly.FIND_WITH_DESKRIPTOREN;

		return entityManager.createNamedQuery(queryId, PersistenteQuelleReadonly.class)
			.setParameter("deskriptoren", wrappedDeskriptoren)
			.getResultList();
	}

	@Override
	public Optional<PersistenteQuelleReadonly> findById(final String id) {

		PersistenteQuelleReadonly result = entityManager.find(PersistenteQuelleReadonly.class, id);

		return result == null ? Optional.empty() : Optional.of(result);
	}
}
