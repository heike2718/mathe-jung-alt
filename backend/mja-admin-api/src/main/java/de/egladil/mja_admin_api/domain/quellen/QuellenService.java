// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.quellen;

import java.util.List;
import java.util.Optional;

import de.egladil.mja_admin_api.domain.dto.Suchfilter;
import de.egladil.web.mja_shared.semantik.DomainService;

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

	/**
	 * Sucht die Quelle mit Quellenart PERSON und dem gegebenen Namen.
	 *
	 * @param  name
	 *              String muss vollständig sein
	 * @return      Optional
	 */
	Optional<QuelleReadonly> sucheAdministrator(String name);
}
