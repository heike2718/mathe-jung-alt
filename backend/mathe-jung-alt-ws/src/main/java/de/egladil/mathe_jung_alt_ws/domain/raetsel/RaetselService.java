// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.RaetselPayloadDaten;

/**
 * RaetselService
 */
public interface RaetselService {

	/**
	 * Legt ein neues Rätsel an.
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	RaetselPayloadDaten raetselAnlegen(EditRaetselPayload payload);

	EditRaetselPayload getRaetselZuId(String id);

}
