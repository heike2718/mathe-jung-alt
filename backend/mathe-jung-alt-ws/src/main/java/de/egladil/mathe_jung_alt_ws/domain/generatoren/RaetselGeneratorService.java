// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.GeneratedImages;

/**
 * RaetselGeneratorService
 */
public interface RaetselGeneratorService {

	/**
	 * Generiert den output des Raetsels im gewünschten Format und gibt die Url als String zurück.
	 *
	 * @param  outputformat
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 *                                   AnzeigeAntwortvorschlaegeTyp
	 * @return
	 */
	GeneratedImages produceOutputReaetsel(Outputformat outputformat, String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

}
