package de.egladil.mja_admin_api.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.quellen.QuellenRepository;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

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
	public Optional<PersistenteQuelleReadonly> findQuelleWithPersonEqual(final String name) {

		List<PersistenteQuelleReadonly> trefferliste = entityManager
			.createNamedQuery(PersistenteQuelleReadonly.FIND_WITH_PERSON_EQUALS, PersistenteQuelleReadonly.class)
			.setParameter("suchstring", name).getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.warn("Es gibt mehr als eine Quelle mit person={} - es wird die erste zurückgegeben", name);
		}

		return trefferliste.isEmpty() ? Optional.empty() : Optional.of(trefferliste.get(0));
	}

	@Override
	public Optional<PersistenteQuelleReadonly> findById(final String id) {

		PersistenteQuelleReadonly result = entityManager.find(PersistenteQuelleReadonly.class, id);

		return result == null ? Optional.empty() : Optional.of(result);
	}

	@Override
	public Optional<PersistenteQuelleReadonly> getDefaultQuelle() {

		List<PersistenteQuelleReadonly> trefferliste = entityManager
			.createNamedQuery(PersistenteQuelleReadonly.FIND_QUELLE_BY_FLAG_HW, PersistenteQuelleReadonly.class)
			.setParameter("hw", true).getResultList();

		if (trefferliste.size() > 1) {

			LOGGER.warn("Blöd, da gibt es mehr als eine Quelle mit hw = 1");
			return Optional.empty();
		}

		return trefferliste.isEmpty() ? Optional.empty() : Optional.of(trefferliste.get(0));
	}

}
