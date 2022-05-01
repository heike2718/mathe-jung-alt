// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren.impl;

import javax.enterprise.context.ApplicationScoped;

import de.egladil.mathe_jung_alt_ws.domain.semantik.Repository;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * DeskriptorenRepository
 */
@Repository
@ApplicationScoped
public class DeskriptorenRepository implements PanacheRepository<Deskriptor> {

}
