// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.deskriptoren.impl;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import de.egladil.raetselbaukasten.domain.semantik.Repository;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.Deskriptor;

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
