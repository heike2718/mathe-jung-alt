// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren;

import java.util.List;
import java.util.Optional;

import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;

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

	/**
	 * Gibt alle Ids sortiert als kommaseparierten String zurück. Dubletten werden zuvor entfernt.
	 *
	 * @param  deskriptoren
	 * @return              String oder null, wenn leere oder null-Liste
	 */
	String sortAndStringifyIdsDeskriptoren(List<Deskriptor> deskriptoren);

	/**
	 * Filtert die gegebenen Deskriptoren nach ihrem Kontext.
	 *
	 * @param  kontext
	 *                      DeskriptorSuchkontext
	 * @param  deskriptoren
	 * @return              List: leer bei NOOP und nicht admin, alle bei NOOP und admin
	 */
	@Deprecated
	List<Deskriptor> filterByKontext(DeskriptorSuchkontext kontext, List<Deskriptor> deskriptoren);

	/**
	 * Sucht den Deskriptor anhand seines Namens.
	 *
	 * @param  name
	 *              String
	 * @return      Optional
	 */
	Optional<Deskriptor> findByName(String name);

	/**
	 * Wandelt die kommaeparierten Namen von Deskriptoren in deren kommaseparierte IDs um.<br>
	 * <br>
	 * <strong>Achtung: </strong> Nicht vorhandenen Namen werden ignoriert.
	 *
	 * @param  deskriptorenNames
	 * @return                   String kommaseparierte IDs
	 */
	String transformToDeskriptorenOrdinal(String deskriptorenNames);

}
