// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * RaetselFileService
 */
public interface RaetselFileService {

	/**
	 * Schreibt die Frage des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des File zurück.
	 *
	 * @param  raetsel
	 *                      Raetsel die UUID des Raetsels
	 * @param  outputformat
	 *                      Outputformat das gewünschte output-format.
	 * @return              String Pfad des LaTeX-Files.
	 */
	String generateFrageLaTeX(Raetsel raetsel, Outputformat outputformat);

}
