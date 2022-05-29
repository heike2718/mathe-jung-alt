// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen;

import java.util.List;
import java.util.Optional;

import de.egladil.mathe_jung_alt_ws.domain.semantik.Repository;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenRepository
 */
@Repository
public interface QuellenRepository {

	/**
	 * Unscharfe Suche nach Quellen zu einem gegeben Medium oder einer Person. Gesucht wird mit like.
	 *
	 * @param  suchstring
	 *                    String
	 * @return            List
	 */
	List<PersistenteQuelleReadonly> findQuellenLikeMediumOrPerson(String suchstring);

	/**
	 * @param  id
	 *            String die id
	 * @return    Optional
	 */
	Optional<PersistenteQuelleReadonly> findById(String id);

}
