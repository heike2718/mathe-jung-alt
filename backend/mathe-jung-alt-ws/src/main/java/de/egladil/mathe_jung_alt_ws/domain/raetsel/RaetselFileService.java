// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

/**
 * RaetselFileService
 */
public interface RaetselFileService {

	/**
	 * Schreibt die Frage des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des File zurück.
	 *
	 * @param  raetselUuid
	 *                     Raetsel die UUID des Raetsels
	 * @return             String Pfad des LaTeX-Files.
	 */
	String generateLaTeXDocumentOfRaetselFrage(Raetsel raetsel);

}
