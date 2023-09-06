// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.List;

import de.egladil.mja_api.domain.DomainEntityStatus;
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
	long countRaetselVolltext(String suchstring, boolean nurFreigegebene);

	/**
	 * Sucht alle Raetsel, deren Deskriptoren die deskriptorenIDs als Teilmenge enthalten.
	 *
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren
	 * @return                 List
	 */
	long countWithDeskriptoren(String deskriptorenIDs, boolean nurFreigegebene);

	/**
	 * Volltextsuche mit Deskriptoren in RAETSEL mit dem gegebenen Suchstring.
	 *
	 * @param  suchstring
	 *                         String Wort.
	 * @param  deskriptorenIDs
	 *                         String die IDs der Deskriptoren, kommasepariert
	 * @return                 List
	 */
	long countRaetselWithFilter(String suchstring, String deskriptorenIDs, boolean nurFreigegebene);

	/**
	 * @param  status
	 * @return        long
	 */
	long countRaetselWithStatus(DomainEntityStatus status);

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
	List<PersistentesRaetsel> findRaetselVolltext(String suchstring, int limit, int offset, SortDirection sortDirection, boolean nurFreigegebene);

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
	List<PersistentesRaetsel> findWithDeskriptoren(String deskriptorenIDs, int limit, int offset, SortDirection sortDirection, boolean nurFreigegebene);

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
	List<PersistentesRaetsel> findRaetselWithFilter(String suchstring, String deskriptorenIDs, int limit, int offset, SortDirection sortDirection, boolean nurFreigegebene);

	/**
	 * @param  schluessel
	 * @return
	 */
	PersistentesRaetsel findWithSchluessel(String schluessel);

	/**
	 * Selectiert alle Raetsel, deren SCHLUESSEL in der gegebenen Collection enthalten ist.
	 *
	 * @param  schluessel
	 *                    List
	 * @return            List
	 */
	List<PersistentesRaetsel> findWithSchluessel(List<String> schluessel);
}
