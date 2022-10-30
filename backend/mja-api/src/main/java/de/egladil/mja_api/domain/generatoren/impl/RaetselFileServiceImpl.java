// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.utils.MjaFileUtils;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final String LATEX_TEMPLATE_PNG_TEX = "/latex/template-raetsel-png.tex";

	private static final String LATEX_TEMPLATE_PDF_TEX = "/latex/template-raetsel-pdf.tex";

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Override
	public String generateFrageLaTeX(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + ".tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(layoutAntwortvorschlaege)
			.generateLaTeXAntwortvorschlaege(raetsel);

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PNG_TEX);
		template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.getPlaceholder(), "");
		template = template.replace(LaTeXPlaceholder.CONTENT.getPlaceholder(), raetsel.getFrage());
		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.getPlaceholder(), antworten);

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public String generateLoesungLaTeX(final Raetsel raetsel) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + SUFFIX_LOESUNGEN + ".tex";
		File file = new File(path);

		String textLoesungsbuchstabe = getTextLoesungsbuchstabe(raetsel.getAntwortvorschlaege());

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PNG_TEX);
		template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.getPlaceholder(), textLoesungsbuchstabe);
		template = template.replace(LaTeXPlaceholder.CONTENT.getPlaceholder(), raetsel.getLoesung());
		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.getPlaceholder(), "");

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public String generateFrageUndLoesung(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + SUFFIX_PDF + ".tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(layoutAntwortvorschlaege)
			.generateLaTeXAntwortvorschlaege(raetsel);

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PDF_TEX);
		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.getPlaceholder(), raetsel.getFrage());
		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.getPlaceholder(), antworten);

		if (StringUtils.isNotBlank(raetsel.getLoesung())) {

			String textLoesungsbuchstabe = getTextLoesungsbuchstabe(raetsel.getAntwortvorschlaege());
			template = template.replace(LaTeXPlaceholder.PAR.getPlaceholder(), LaTeXConstants.VALUE_PAR);
			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.getPlaceholder(), textLoesungsbuchstabe);
			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.getPlaceholder(), raetsel.getLoesung());
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
