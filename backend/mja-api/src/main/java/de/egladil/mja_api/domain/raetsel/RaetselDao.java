// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.List;

import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselDao
 */
public interface RaetselDao {

	/**
	 * Volltextsuche in RAETSEL mit dem gegebenen Suchstring.
	 *
	 * @param  suchstring
	 * @return            List
	 */
	long zaehleRaetselVolltext(String suchstring);

	/**
	 * Sucht alle Raetsel, deren Deskriptoren die deskriptorenIDs als Teilmenge enthalten.
	 *
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren
	 * @return                 List
	 */
	long zaehleMitDeskriptoren(String deskriptorenIDs);

	/**
	 * Volltextsuche mit Deskriptoren in RAETSEL mit dem gegebenen Suchstring.
	 *
	 * @param  suchstring
	 *                         String Wort.
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren, kommasepariert
	 * @return                 List
	 */
	long zaehleRaetselComplete(String suchstring, String deskriptorenIDs);

	/**
	 * Volltextsuche in RAETSEL mit dem gegebenen Suchstring.
	 *
	 * @param  suchstring
	 * @param  limit
	 *                       int Anzahl Treffer in page
	 * @param  offset
	 *                       int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                       SortDirection asc/desc
	 * @return               List
	 */
	List<PersistentesRaetsel> sucheRaetselVolltext(String suchstring, int limit, int offset, SortDirection sortDirection);

	/**
	 * Sucht alle Raetsel, deren Deskriptoren die deskriptorenIDs als Teilmenge enthalten.
	 *
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren
	 * @param  limit
	 *                         int Anzahl Treffer in page
	 * @param  offset
	 *                         int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                         SortDirection asc/desc
	 * @return                 List
	 */
	List<PersistentesRaetsel> sucheMitDeskriptoren(String deskriptorenIDs, int limit, int offset, SortDirection sortDirection);

	/**
	 * Volltextsuche mit Deskriptoren in RAETSEL mit dem gegebenen Suchstring.
	 *
	 * @param  suchstring
	 *                         String Wort.
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren, kommasepariert
	 * @param  limit
	 *                         int Anzahl Treffer in page
	 * @param  offset
	 *                         int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                         SortDirection asc/desc
	 * @return                 List
	 */
	List<PersistentesRaetsel> sucheRaetselComplete(String suchstring, String deskriptorenIDs, int limit, int offset, SortDirection sortDirection);

}
