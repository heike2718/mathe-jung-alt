// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen;

import java.util.List;
import java.util.Optional;

import de.egladil.mathe_jung_alt_ws.domain.dto.Suchfilter;
import de.egladil.mathe_jung_alt_ws.domain.semantik.DomainService;

/**
 * QuellenService
 */
@DomainService
public interface QuellenService {

	/**
	 * Sucht quellen mit dem gegebenen suchfilter.
	 *
	 * @param  suchfilter
	 *                    Suchfilter
	 * @return            List
	 */
	List<QuelleReadonly> sucheQuellen(Suchfilter suchfilter);

	/**
	 * Sucht die Quelle mit der gegebenen id.
	 *
	 * @param  id
	 *            String
	 * @return    Optional
	 */
	Optional<QuelleReadonly> sucheQuelleMitId(String id);
}
