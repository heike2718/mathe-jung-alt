// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.AnzeigeAntwortvorschlaegeTyp;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;

/**
 * RaetselGeneratorService
 */
public interface RaetselGeneratorService {

	/**
	 * Generiert den output des Raetsels im gewünschten Format und gibt die Url als String zurück.
	 *
	 * @param  outputformat
	 * @param  raetselUuid
	 * @param  anzeigeAntwortvorschlaege
	 *                                   AnzeigeAntwortvorschlaegeTyp
	 * @return
	 */
	String produceOutputReaetsel(Outputformat outputformat, String raetselUuid, AnzeigeAntwortvorschlaegeTyp anzeigeAntwortvorschlaege);

}
