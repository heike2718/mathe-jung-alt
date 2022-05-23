// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.EditRaetselPayload;

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
	Raetsel raetselAnlegen(EditRaetselPayload payload);

	/**
	 * Holt die Details des Rätsels zu der gegebenen id.
	 *
	 * @param  id
	 * @return    Raetsel oder null.
	 */
	Raetsel getRaetselZuId(String id);

}
