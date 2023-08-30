// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;

/**
 * RaetselFileService
 */
public interface RaetselFileService {

	final String SUFFIX_LOESUNGEN = "_l";

	final String SUFFIX_PDF = "_x";

	/**
	 * Schreibt die Frage des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des File zurück. Der Name des
	 * Files ist der Schlüssel des Raetsels.
	 *
	 * @param  raetsel
	 *                                  Raetsel
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @param  font
	 *                                  FontName in welchem font generiert werden soll.
	 * @return                          String Pfad des LaTeX-Files.
	 */
	String generateFrageLaTeX(Raetsel raetsel, LayoutAntwortvorschlaege layoutAntwortvorschlaege, FontName font);

	/**
	 * Schreibt die Lösung des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des Files zurück. Der Name des
	 * Files ist der Schlüssel des Raetsels mit angehängtem _l.
	 *
	 * @param  raetsel
	 * @param  font
	 *                 FontName in welchem font generiert werden soll.
	 * @return         String Pfad des LaTeX-Files.
	 */
	String generateLoesungLaTeX(Raetsel raetsel, final FontName font);

	/**
	 * Generiert das LaTeX für die PDF-Vorschau des gegebenen Raetsels, speichert es als LaTeX-File im Filessystem und gibt den Pfad
	 * des Files zurück.
	 *
	 * @param  raetsel
	 *                                  Der Name des Files ist der Schlüssel des Raetsels mit angehängtem _x.
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @param  font
	 *                                  FontName in welchem font generiert werden soll.
	 * @return                          String
	 */
	String generiereLaTeXRaetselPDF(Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font);

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
	 * Schaut, ob die Images für Frage und Lösung bereits da sind und verpackt sie in ein Objekt.
	 *
	 * @param  schluessel
	 * @return
	 */
	Images findImages(String schluessel);

	/**
	 * Falls das PDF bereits generiert wurde, wird es aus dem Dateisystem gelesen.
	 *
	 * @param  schluessel
	 * @return            byte[]
	 */
	byte[] findPDF(String schluessel);

	/**
	 * Falls es schluessel.log oder schluessel_l.log gibt, werden sie in das Array verpackt zum Downloaden.
	 *
	 * @param  schluessel
	 * @return
	 */
	GeneratedFile[] getLaTeXLogs(String schluessel);

	/**
	 * Prüft, ob die Datei existiert.
	 *
	 * @param  relativerPfad
	 * @return
	 */
	boolean existsGrafik(String relativerPfad);

	/**
	 * Löscht die temporären Files aus dem latex-basedir.
	 *
	 * @param filenames
	 *                  String oder String[]
	 */
	void deleteTemporaryFiles(String... filenames);
}
