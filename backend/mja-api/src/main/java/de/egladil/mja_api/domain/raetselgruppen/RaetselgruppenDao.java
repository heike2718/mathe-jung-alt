// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import java.util.List;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;

/**
 * RaetselgruppenDao
 */
@Repository
public interface RaetselgruppenDao {

	/**
	 * Zählt die Treffermenge bei Anwendung des gegebenen Filters. Die Parameter können null sein. Bei name und kommentar wird mit
	 * like gesucht, alle anderen mit equal.
	 *
	 * @param  name
	 * @param  kommentar
	 * @param  schwierigkeitsgrad
	 * @param  referenztyp
	 * @param  referenz
	 * @param  status
	 * @return
	 */
	long countByFilter(String name, String kommentar, Schwierigkeitsgrad schwierigkeitsgrad, Referenztyp referenztyp, String referenz, DomainEntityStatus status);

	/**
	 * Sucht die Rätselgruppen die den Filterkriterien entsprechen, sortiert nach name. Die Parameter können null sein. Bei name
	 * und kommentar wird mit like gesucht, alle anderen mit equal.
	 *
	 * @param  name
	 * @param  kommentar
	 * @param  schwierigkeitsgrad
	 * @param  referenztyp
	 * @param  referenz
	 * @param  status
	 * @param  limit
	 * @param  offset
	 * @param  sortDirection
	 * @return
	 */
	List<PersistenteRaetselgruppe> findByFilter(String name, String kommentar, Schwierigkeitsgrad schwierigkeitsgrad, Referenztyp referenztyp, String referenz, DomainEntityStatus status, int limit, int offset, SortDirection sortDirection);

	/**
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    PersistenteRaetselgruppe oder null
	 */
	PersistenteRaetselgruppe findByUniqueKey(Referenztyp referenztyp, String referenz, Schwierigkeitsgrad schwierigkeitsgrad);

	/**
	 * @param  name
	 * @return      PersistenteRaetselgruppe oder null
	 */
	PersistenteRaetselgruppe findByName(String name);

	/**
	 * Zählt die Elemente der Rätselgruppe.
	 *
	 * @param  gruppeID
	 * @return
	 */
	long countElementeRaetselgruppe(String gruppeID);

	/**
	 * Gibt alle Elemente der gegebenen Rästelgruppe zurück.
	 *
	 * @param  gruppeID
	 * @return          List
	 */
	List<PersistentesRaetselgruppenelement> loadElementeRaetselgruppe(String gruppeID);

	/**
	 * Läd die Aufagben mit den gegebenen UUIDs
	 *
	 * @param  uuids
	 * @return
	 */
	List<PersistenteAufgabeReadonly> loadAufgabenByRaetselIds(List<String> uuids);

	/**
	 * Speichert die gegebene Rätselgruppe und gibt die gespeicherte Entity zurück.
	 *
	 * @param  gruppe
	 *                PersistenteRaetselgruppe
	 * @return        PersistenteRaetselgruppe
	 */
	PersistenteRaetselgruppe saveRaetselgruppe(PersistenteRaetselgruppe gruppe);
}
