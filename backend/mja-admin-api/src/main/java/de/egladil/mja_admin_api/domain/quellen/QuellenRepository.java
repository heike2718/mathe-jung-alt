// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.quellen;

import java.util.List;
import java.util.Optional;

import de.egladil.mja_admin_api.domain.semantik.Repository;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

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
	 * @param  deskriptorenIDs
	 * @return
	 */
	List<PersistenteQuelleReadonly> findWithDeskriptoren(String deskriptorenIDs);

	/**
	 * Exakte Suche nach person.
	 *
	 * @param  name
	 *              String
	 * @return      Optional
	 */
	Optional<PersistenteQuelleReadonly> findQuelleWithPersonEqual(String name);

	/**
	 * @param  id
	 *            String die id
	 * @return    Optional
	 */
	Optional<PersistenteQuelleReadonly> findById(String id);

	/**
	 * Die default-Quelle bin ich.
	 *
	 * @return Optional
	 */
	Optional<PersistenteQuelleReadonly> getDefaultQuelle();

}
