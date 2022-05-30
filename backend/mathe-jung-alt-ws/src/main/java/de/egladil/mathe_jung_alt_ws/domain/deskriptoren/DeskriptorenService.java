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

	/**
	 * Gibt alle Ids sortiert als kommaseparierten Sring zurück.
	 *
	 * @param  deskriptoren
	 * @return              String oder null, wenn leere oder null-Liste
	 */
	String sortAndStringifyIdsDeskriptoren(List<Deskriptor> deskriptoren);

	/**
	 * Filtert die gegebenen Deskriptoren nach ihrem Kontext.
	 *
	 * @param  kontext
	 * @param  deskriptoren
	 * @return              List
	 */
	List<Deskriptor> filterByKontext(DeskriptorSuchkontext kontext, List<Deskriptor> deskriptoren);

	/**
	 * Mappt den kontext auf die Enum.
	 *
	 * @param  kontext
	 *                 String
	 * @return         NOOP, wenn null oder invalid
	 */
	DeskriptorSuchkontext toDeskriptorSuchkontext(String kontext);

}
