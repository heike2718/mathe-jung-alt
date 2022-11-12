// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.List;
import java.util.Optional;

import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.semantik.DomainService;

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
	 * Sucht die Quelle mit der gegebenen userId.
	 *
	 * @param  userId
	 * @return        Optional
	 */
	Optional<QuelleReadonly> sucheQuelleMitUserID(String userId);
}
