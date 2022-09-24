// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	 * @param  suchparameter
	 * @return               long
	 */
	long countByFilter(RaetselgruppenSuchparameter suchparameter);

	/**
	 * Sucht die Rätselgruppen die den Filterkriterien entsprechen, sortiert nach name. Die Parameter können null sein. Bei name
	 * und kommentar wird mit like gesucht, alle anderen mit equal.
	 *
	 * @param  suchparameter
	 * @param  limit
	 * @param  offset
	 * @return               List
	 */
	List<PersistenteRaetselgruppe> findByFilter(RaetselgruppenSuchparameter suchparameter, int limit, int offset);

	/**
	 * @param  raetselgruppeID
	 * @return
	 */
	PersistenteRaetselgruppe findByID(String raetselgruppeID);

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

	/**
	 * @param  uuid
	 * @return
	 */
	long countElementeRaetselgruppe(@NotNull @Size(min = 1, max = 40) String uuid);
}
