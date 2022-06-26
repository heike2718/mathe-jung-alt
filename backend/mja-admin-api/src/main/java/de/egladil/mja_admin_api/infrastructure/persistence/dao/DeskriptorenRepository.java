// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.dao;

import javax.enterprise.context.ApplicationScoped;

import de.egladil.mja_admin_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_shared.semantik.Repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * DeskriptorenRepository
 */
@Repository
@ApplicationScoped
public class DeskriptorenRepository implements PanacheRepository<Deskriptor> {

}
