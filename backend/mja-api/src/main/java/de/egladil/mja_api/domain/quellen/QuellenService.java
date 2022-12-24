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
	 * Sucht Quellen mitdem gegebenen Suchstring im Namen.
	 *
	 * @param  suchstring
	 * @return            List
	 */
	List<QuellenListItem> findQuellen(String suchstring);

	/**
	 * Sucht quellen mit dem gegebenen suchfilter.
	 *
	 * @param  suchfilter
	 *                    Suchfilter
	 * @return            List
	 */
	@Deprecated
	List<QuellenListItem> sucheQuellen(Suchfilter suchfilter);

	/**
	 * Sucht die Quelle mit der gegebenen id.
	 *
	 * @param  id
	 *            String
	 * @return    Optional
	 */
	Optional<QuellenListItem> sucheQuelleMitId(String id);

	/**
	 * Sucht die Quelle mit der gegebenen userId.
	 *
	 * @param  userId
	 * @return        Optional
	 */
	@Deprecated
	Optional<QuellenListItem> sucheQuelleMitUserID(String userId);

	/**
	 * Sucht die Quelle mit der gegebenen userId.
	 *
	 * @param  userId
	 * @return        Optional
	 */
	Optional<QuelleMinimalDto> findQuelleForUser(String userId);
}
