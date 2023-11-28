// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren.impl;

import java.util.List;

import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * DeskriptorenRepository
 */
@Repository
@ApplicationScoped
public class DeskriptorenRepository {

	@Inject
	EntityManager entityManager;

	/**
	 * @return
	 */
	public List<Deskriptor> listAll() {

		return entityManager.createNamedQuery(Deskriptor.LIST_ALL, Deskriptor.class).getResultList();
	}

}
