// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren;

import java.util.List;

import de.egladil.mathe_jung_alt_ws.domain.semantik.DomainService;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * DeskriptorenService
 */
@DomainService
public interface DeskriptorenService {

	/**
	 * @param  deskriptorenIds
	 * @return
	 */
	List<Deskriptor> mapToDeskriptoren(final String deskriptorenIds);

}
