// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen;

import java.util.List;

import de.egladil.mathe_jung_alt_ws.domain.semantik.DomainService;

/**
 * QuellenService
 */
@DomainService
public interface QuellenService {

	/**
	 * Sucht nach Quellen, bei denen der suchtring im Feld MEDIENT_TITEL oder PERSON vorkommt.
	 *
	 * @param  suchstring
	 * @return            List
	 */
	List<QuelleReadonly> sucheQuellen(String suchstring);
}
