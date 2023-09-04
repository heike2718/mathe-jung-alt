// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.TrennerartFrageLoesung;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.utils.GeneratorUtils;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Override
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

	@Override
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

	@Override
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

	@Override
	public byte[] findImageFrage(final String schluessel) {

		String path = latexBaseDir + File.separator + schluessel + ".png";
		return MjaFileUtils.loadBinaryFile(path, false);
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

		MjaFileUtils.writeOutput(file, template, errorMessage);
	}

	@Override
	public byte[] findImageLoesung(final String schluessel) {

		String path = latexBaseDir + File.separator + schluessel + SUFFIX_LOESUNGEN + ".png";
		return MjaFileUtils.loadBinaryFile(path, false);
	}

	@Override
	public Images findImages(final String schluessel) {

		return new Images().withImageFrage(findImageFrage(schluessel)).withImageLoesung(findImageLoesung(schluessel));
	}

	@Override
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

	@Override
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

	@Override
	public boolean existsGrafik(final String relativerPfad) {

		String path = latexBaseDir + relativerPfad;
		File file = new File(path);

		return file.exists() && file.isFile();
	}

	@Override
	public void deleteTemporaryFiles(final String... filenames) {

		for (String filename : filenames) {

			String path = latexBaseDir + File.separator + filename;
			boolean deleted = new File(path).delete();

			if (!deleted) {

				LOGGER.warn("File {} wurde nicht gelöscht", path);
			}
		}
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

}
