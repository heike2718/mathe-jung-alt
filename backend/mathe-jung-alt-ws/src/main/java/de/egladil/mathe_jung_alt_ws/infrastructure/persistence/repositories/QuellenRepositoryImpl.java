package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.repositories;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.egladil.mathe_jung_alt_ws.domain.quellen.QuellenRepository;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

@ApplicationScoped
public class QuellenRepositoryImpl implements QuellenRepository {

	@Inject
	EntityManager entityManager;

	@Override
	public List<PersistenteQuelleReadonly> findQuellenLikeMediumOrPerson(final String suchstring) {

		String text = "%" + suchstring.toLowerCase() + "%";

		return entityManager.createNamedQuery(PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON, PersistenteQuelleReadonly.class)
			.setParameter("suchstring", text).getResultList();
	}

}
