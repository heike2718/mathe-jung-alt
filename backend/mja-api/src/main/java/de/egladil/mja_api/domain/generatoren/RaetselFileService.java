// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.generatoren.impl.AntwortvorschlagGeneratorStrategegy;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXPlaceholder;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXTemplatesService;
import de.egladil.mja_api.domain.generatoren.impl.LoesungsbuchstabeTextGenerator;
import de.egladil.mja_api.domain.generatoren.impl.QuizitemLaTeXGenerator;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.utils.GeneratorUtils;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * RaetselFileService
 */
@ApplicationScoped
public class RaetselFileService {

	private static final String FIRST_SUBDIR = "/vorschau/";

	public static final String SUFFIX_LOESUNGEN = "_l";

	public static final String SUFFIX_PDF = "_x";

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileService.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

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
	public String generateFrageLaTeX(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font, final Schriftgroesse schriftgroesse) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + ".tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(layoutAntwortvorschlaege)
			.generateLaTeXAntwortvorschlaege(raetsel.getAntwortvorschlaege());

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentRaetselPNG();

		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), font.getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), schriftgroesse.getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(), schriftgroesse.getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), "");
		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), raetsel.getFrage());
		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), antworten);

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

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
	public String generateLoesungLaTeX(final Raetsel raetsel, final FontName font, final Schriftgroesse schriftgroesse) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + SUFFIX_LOESUNGEN + ".tex";
		File file = new File(path);

		String textLoesungsbuchstabe = getTextLoesungsbuchstabe(raetsel.getAntwortvorschlaege());

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentRaetselPNG();

		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), font.getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), schriftgroesse.getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(), schriftgroesse.getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), textLoesungsbuchstabe);
		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), raetsel.getLoesung());
		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), "");

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

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
	public String generiereLaTeXRaetselPDF(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font, final Schriftgroesse schriftgroesse) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + SUFFIX_PDF + ".tex";
		File file = new File(path);

		RaetselGeneratorinput input = new RaetselGeneratorinput()
			.withAntwortvorschlaege(raetsel.getAntwortvorschlaege())
			.withFrage(raetsel.getFrage())
			.withLoesung(raetsel.getLoesung())
			.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege)
			.withNummer(raetsel.getSchluessel())
			.withVerwendungszweck(Verwendungszweck.LATEX)
			.withSchluessel(raetsel.getSchluessel());

		boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(layoutAntwortvorschlaege,
			input);

		String textRaetsel = new QuizitemLaTeXGenerator().generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND,
			printAsMultipleChoice);

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentRaetselPDF();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), schriftgroesse.getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			schriftgroesse.getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), font.getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), textRaetsel);

		switch (font) {

		case DRUCK_BY_WOK:

			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(),
				LaTeXTemplatesService.getInstance().getLizenzFontsDruckschrift());
			break;

		case FIBEL_NORD:
		case FIBEL_SUED:
			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(),
				LaTeXTemplatesService.getInstance().getLizenzFontsFibel());
			break;

		case STANDARD:
			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(), "");
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + font);
		}

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	/**
	 * @param  filename
	 * @return          byte[] oder null
	 */
	public byte[] findVorschau(final String filename) {

		String path = latexBaseDir + FIRST_SUBDIR + filename.substring(0, 1) + File.separator + filename;
		return MjaFileUtils.loadBinaryFile(path, false);
	}

	/**
	 * Nach dem generieren der PNGs liegen diese im latex.base.dir (Namen schluessel.png bzw. schluessel_l.png). Diese Methode
	 * verschiebt die generierten PNGs in das entsprechende Unterverzeichnis.
	 *
	 * @param raetsel
	 *                Raetsel
	 */
	public void moveVorschau(final Raetsel raetsel) {

		moveVorschauFrage(raetsel);
		moveVorschauLoesung(raetsel);
	}

	/**
	 * Schaut, ob die Images für Frage und Lösung bereits da sind und verpackt sie in ein Objekt.
	 *
	 * @param  filenameFrage
	 *                         String
	 * @param  filenameLoesung
	 *                         String
	 * @return
	 */
	public Images findImages(final String filenameFrage, final String filenameLoesung) {

		return new Images().withImageFrage(findVorschau(filenameFrage)).withImageLoesung(findVorschau(filenameLoesung));
	}

	/**
	 * Falls das PDF bereits generiert wurde, wird es aus dem Dateisystem gelesen.
	 *
	 * @param  schluessel
	 * @return            byte[]
	 */
	public byte[] findPDF(final String schluessel) {

		String path = latexBaseDir + File.separator + schluessel + SUFFIX_PDF + ".pdf";
		return MjaFileUtils.loadBinaryFile(path, true);

	}

	byte[] findLaTeXLogAufgabe(final String schluessel) {

		String path = latexBaseDir + File.separator + schluessel + ".log";
		return MjaFileUtils.loadBinaryFile(path, false);
	}

	byte[] findLaTeXLogLoesung(final String schluessel) {

		String path = latexBaseDir + File.separator + schluessel + SUFFIX_LOESUNGEN + ".log";
		return MjaFileUtils.loadBinaryFile(path, false);
	}

	/**
	 * Falls es schluessel.log oder schluessel_l.log gibt, werden sie in das Array verpackt zum Downloaden.
	 *
	 * @param  schluessel
	 * @return
	 */
	public GeneratedFile[] getLaTeXLogs(final String schluessel) {

		byte[] aufgabeLog = this.findLaTeXLogAufgabe(schluessel);
		byte[] loesungLog = this.findLaTeXLogLoesung(schluessel);

		if (aufgabeLog != null && loesungLog != null) {

			GeneratedFile[] result = new GeneratedFile[2];
			result[0] = new GeneratedFile(schluessel + ".log", aufgabeLog);
			result[1] = new GeneratedFile(schluessel + SUFFIX_LOESUNGEN + ".log", loesungLog);
			return result;
		}

		if (aufgabeLog != null) {

			return new GeneratedFile[] { new GeneratedFile(schluessel + ".log", aufgabeLog) };
		}

		if (loesungLog != null) {

			return new GeneratedFile[] { new GeneratedFile(schluessel + SUFFIX_LOESUNGEN + ".log", loesungLog) };
		}

		return new GeneratedFile[0];
	}

	/**
	 * Prüft, ob die Datei existiert.
	 *
	 * @param  relativerPfad
	 * @return
	 */
	public boolean fileExists(final String relativerPfad) {

		String path = latexBaseDir + relativerPfad;
		File file = new File(path);

		return file.exists() && file.isFile();
	}

	/**
	 * Löscht die Files mit den gegebenen Namen aus dem latex-basedir.
	 *
	 * @param filenames
	 *                  String oder String[]
	 */
	public void deleteTemporaryFiles(final String... filenames) {

		for (String filename : filenames) {

			String path = latexBaseDir + File.separator + filename;
			boolean deleted = new File(path).delete();

			if (!deleted) {

				LOGGER.warn("File {} wurde nicht gelöscht", path);
			}
		}
	}

	/**
	 * Löscht die gegebene Datei im Filesystem.
	 *
	 * @param relativePath
	 *                     String Pfad relativ zum latex.base.dir
	 */
	public boolean deleteImageFile(final String relativePath) {

		Path path = Path.of(this.latexBaseDir + File.separator + relativePath);

		try {

			java.nio.file.Files.delete(path);

			return true;
		} catch (IOException e) {

			LOGGER.error(e.getMessage(), e);

			String message = "Datei " + path + " konnte nicht geloescht werden";

			throw new MjaRuntimeException(message);

		}
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

	/**
	 * @param  raetsel
	 * @param  file
	 * @param  template
	 * @return          String
	 */
	private void writeOutput(final Raetsel raetsel, final File file, final String template) {

		String errorMessage = "konnte kein LaTex-File schreiben Raetsel: [schluessel=" + raetsel.getSchluessel()
			+ ", uuid="
			+ raetsel.getId() + "]";

		MjaFileUtils.writeTextfile(file, template, errorMessage);
	}

	private void moveVorschauLoesung(final Raetsel raetsel) {

		String pfad = latexBaseDir + File.separator + raetsel.getSchluessel() + SUFFIX_LOESUNGEN
			+ Outputformat.PNG.getFilenameExtension();
		File source = new File(pfad);

		if (source.exists()) {

			String filenameVorschauLoesung = raetsel.getFilenameVorschauLoesung();
			String targetPfad = latexBaseDir + FIRST_SUBDIR + filenameVorschauLoesung.substring(0, 1) + File.separator
				+ filenameVorschauLoesung;

			File target = new File(targetPfad);
			MjaFileUtils.moveFile(source, target);
		} else {

			LOGGER.warn("{} existiert nicht und wird folglich nicht verschoben", source.getAbsolutePath());
		}
	}

	private void moveVorschauFrage(final Raetsel raetsel) {

		String pfad = latexBaseDir + File.separator + raetsel.getSchluessel() + Outputformat.PNG.getFilenameExtension();
		File source = new File(pfad);

		if (source.exists()) {

			String filenameVorschauFrage = raetsel.getFilenameVorschauFrage();
			String targetPfad = latexBaseDir + FIRST_SUBDIR + filenameVorschauFrage.substring(0, 1) + File.separator
				+ filenameVorschauFrage;

			File target = new File(targetPfad);
			MjaFileUtils.moveFile(source, target);
		} else {

			LOGGER.warn("{} existiert nicht und wird folglich nicht verschoben", source.getAbsolutePath());
		}
	}

}
