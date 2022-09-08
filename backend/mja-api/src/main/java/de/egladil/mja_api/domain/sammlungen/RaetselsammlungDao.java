// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.sammlungen;

import java.util.List;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.semantik.Repository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselsammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselsammlungselement;

/**
 * RaetselsammlungDao
 */
@Repository
public interface RaetselsammlungDao {

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
	 * Sucht die Rätselsammlungen die den Filterkriterien entsprechen, sortiert nach name. Die Parameter können null sein. Bei name
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
	List<PersistenteRaetselsammlung> findByFilter(String name, String kommentar, Schwierigkeitsgrad schwierigkeitsgrad, Referenztyp referenztyp, String referenz, DomainEntityStatus status, int limit, int offset, SortDirection sortDirection);

	/**
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    PersistenteRaetselsammlung oder null
	 */
	PersistenteRaetselsammlung findByUniqueKey(Referenztyp referenztyp, String referenz, Schwierigkeitsgrad schwierigkeitsgrad);

	/**
	 * @param  name
	 * @return      PersistenteRaetselsammlung oder null
	 */
	PersistenteRaetselsammlung findByName(String name);

	/**
	 * Zählt die Elemente der Rätselsammlung.
	 *
	 * @param  raetselsammlungID
	 * @return
	 */
	long countElementeZuRaetselsammlung(String raetselsammlungID);

	/**
	 * Gibt alle Elemente der gegebenen Rätselsammlung zurück.
	 *
	 * @param  raetselsammlungID
	 * @return                   List
	 */
	List<PersistentesRaetselsammlungselement> loadElementeZuRaetselsammlung(String raetselsammlungID);

	/**
	 * Läd die Aufagben mit den gegebenen UUIDs
	 *
	 * @param  uuids
	 * @return
	 */
	List<PersistenteAufgabeReadonly> loadAufgabenByRaetselIds(List<String> uuids);

	/**
	 * Speichert die gegebene Rätselsammlung und gibt die gespeicherte Entity zurück.
	 *
	 * @param  sammlung
	 * @return          PersistenteRaetselsammlung
	 */
	PersistenteRaetselsammlung saveRaetselsammlung(PersistenteRaetselsammlung sammlung);
}
