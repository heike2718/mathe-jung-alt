// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren;

import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;

/**
 * RaetselFileService
 */
public interface RaetselFileService {

	/**
	 * Schreibt die Frage des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des File zurück. Der Name des
	 * Files ist der Schlüssel des Raetsels.
	 *
	 * @param  raetsel
	 *                                  Raetsel
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          String Pfad des LaTeX-Files.
	 */
	String generateFrageLaTeX(Raetsel raetsel, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Schreibt die Lösung des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des Files zurück. Der Name des
	 * Files ist der Schlüssel des Raetsels mit angehängtem _l.
	 *
	 * @param  raetsel
	 * @return         String Pfad des LaTeX-Files.
	 */
	String generateLoesungLaTeX(Raetsel raetsel);

	/**
	 * Schreibt frage und (falls existent) Lösung in das gleiche LaTeX-File im Filessystem und gibt den Pfad des Files zurück.
	 *
	 * @param  raetsel
	 *                                  Der Name des Files ist der Schlüssel des Raetsels mit angehängtem _x.
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return                          String
	 */
	String generateFrageUndLoesung(Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Falls das png bereits generiert wurde, wird es aus dem Dateisystem gelesen.
	 *
	 * @param  schluessel
	 * @return
	 */
	byte[] findImageFrage(String schluessel);

	/**
	 * Falls das png bereits generiert wurde, wird es aus dem Dateisystem gelesen.
	 *
	 * @param  schluessel
	 * @return
	 */
	byte[] findImageLoesung(String schluessel);

	/**
	 * Falls das PDF bereits generiert wurde, wird es aus dem Dateisystem gelesen.
	 *
	 * @param  schluessel
	 * @return            byte[]
	 */
	byte[] findPDF(String schluessel);

	/**
	 * Prüft, ob die Datei existiert.
	 *
	 * @param  relativerPfad
	 * @return
	 */
	boolean existsGrafik(String relativerPfad);
}
