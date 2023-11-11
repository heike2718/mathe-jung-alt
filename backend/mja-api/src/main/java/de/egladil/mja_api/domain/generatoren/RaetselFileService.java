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
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @return                          String Pfad des LaTeX-Files.
	 */
	String generateFrageLaTeX(Raetsel raetsel, LayoutAntwortvorschlaege layoutAntwortvorschlaege, FontName font, Schriftgroesse schriftgroesse);

	/**
	 * Schreibt die Lösung des gegebenen Raetsels in ein LaTeX-File im Filesystem und gibt den Pfad des Files zurück. Der Name des
	 * Files ist der Schlüssel des Raetsels mit angehängtem _l.
	 *
	 * @param  raetsel
	 * @param  font
	 *                        FontName in welchem font generiert werden soll.
	 * @param  schriftgroesse
	 *                        Schriftgroesse
	 * @return                String Pfad des LaTeX-Files.
	 */
	String generateLoesungLaTeX(Raetsel raetsel, final FontName font, Schriftgroesse schriftgroesse);

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
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @return                          String
	 */
	String generiereLaTeXRaetselPDF(Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font, Schriftgroesse schriftgroesse);

	/**
	 * @param  filename
	 * @return          byte[] oder null
	 */
	byte[] findVorschau(String filename);

	/**
	 * Schaut, ob die Images für Frage und Lösung bereits da sind und verpackt sie in ein Objekt.
	 *
	 * @param  filenameFrage
	 *                         String
	 * @param  filenameLoesung
	 *                         String
	 * @return
	 */
	Images findImages(String filenameFrage, String filenameLoesung);

	/**
	 * Nach dem generieren der PNGs liegen diese im latex.base.dir (Namen schluessel.png bzw. schluessel_l.png). Diese Methode
	 * verschiebt die generierten PNGs in das entsprechende Unterverzeichnis.
	 *
	 * @param raetsel
	 *                Raetsel
	 */
	void moveVorschau(Raetsel raetsel);

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
	boolean fileExists(String relativerPfad);

	/**
	 * Löscht die Files mit den gegebenen Namen aus dem latex-basedir.
	 *
	 * @param filenames
	 *                  String oder String[]
	 */
	void deleteTemporaryFiles(String... filenames);

	/**
	 * Löscht die gegebene Datei im Filesystem.
	 *
	 * @param relativePath
	 *                     String Pfad relativ zum latex.base.dir
	 */
	boolean deleteImageFile(String relativePath);
}
