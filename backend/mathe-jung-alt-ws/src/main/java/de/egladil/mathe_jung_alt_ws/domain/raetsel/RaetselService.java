// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import java.util.List;

import de.egladil.mathe_jung_alt_ws.domain.dto.SortDirection;
import de.egladil.mathe_jung_alt_ws.domain.dto.Suchfilter;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.RaetselsucheTreffer;

/**
 * RaetselService
 */
public interface RaetselService {

	/**
	 * Ermittelt die Anzahl der RAETSEL, auf die der gegebene Suchfilter passt.
	 *
	 * @param  suchfilter
	 * @return            long
	 */
	long zaehleRaetselMitSuchfilter(Suchfilter suchfilter);

	/**
	 * Sucht alle Rätsel, die zum Suchfilter passen.
	 *
	 * @param  suchfilter
	 * @param  limit
	 *                       int Anzahl Treffer in page
	 * @param  offset
	 *                       int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                       SortDirection nach schluessel
	 * @return               List
	 */
	List<RaetselsucheTreffer> sucheRaetsel(Suchfilter suchfilter, final int limit, final int offset, SortDirection sortDirection);

	/**
	 * Legt ein neues Rätsel an.
	 *
	 * @param  payload
	 *                             EditRaetselPayload die Daten und Metainformationen
	 * @param  uuidAendernderUser:
	 *                             String
	 * @return                     RaetselPayloadDaten mit einer generierten UUID.
	 */
	Raetsel raetselAnlegen(EditRaetselPayload payload, String uuidAendernderUser);

	/**
	 * Ändert ein vorhandenes Raetsel
	 *
	 * @param  payload
	 *                             EditRaetselPayload die Daten und Metainformationen
	 * @param  uuidAendernderUser:
	 *                             String
	 * @return                     RaetselPayloadDaten mit einer generierten UUID.
	 */
	Raetsel raetselAendern(EditRaetselPayload payload, String uuidAendernderUser);

	/**
	 * Holt die Details des Rätsels zu der gegebenen id.
	 *
	 * @param  id
	 * @return    Raetsel oder null.
	 */
	Raetsel getRaetselZuId(String id);

}
