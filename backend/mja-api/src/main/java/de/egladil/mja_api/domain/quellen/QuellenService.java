// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.List;
import java.util.Optional;

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
	 * @return Optional
	 */
	Optional<QuelleMinimalDto> findQuelleForUser();

	/**
	 * Läd die minimalen Attribute einer Quelle.
	 *
	 * @param  quelleId
	 * @return          QuelleMinimalDto
	 */
	Optional<QuelleMinimalDto> loadQuelleMinimal(String quelleId);
}
