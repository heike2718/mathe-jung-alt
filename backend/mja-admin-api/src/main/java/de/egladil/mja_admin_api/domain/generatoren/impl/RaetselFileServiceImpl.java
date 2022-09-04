// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_admin_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;
import de.egladil.mja_admin_api.domain.utils.MjaFileUtils;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final String LATEX_TEMPLATE_PNG_TEX = "/latex/template-png.tex";

	private static final String LATEX_TEMPLATE_PDF_TEX = "/latex/template-pdf.tex";

	private static final String PLACEHOLDER_ANTWORTEN = "{antwortvorschlaege}";

	private static final String PLACEHOLDER_LOESUNGSBUCHSTABE = "{loesungsbuchstabe}";

	private static final String PLACEHOLDER_CONTENT = "{content}";

	private static final String PLACEHOLDER_CONTENT_FRAGE = "{content-frage}";

	private static final String PLACEHOLDER_CONTENT_LOESUNG = "{content-loesung}";

	private static final String PLACEHOLDER_NEWPAGE = "{newpage}";

	private static final String VALUE_NEWPAGE = "\\newpage";

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "images.base.dir")
	String imagesBaseDir;

	@Override
	public String generateFrageLaTeX(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + ".tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(layoutAntwortvorschlaege)
			.generateLaTeXAntwortvorschlaege(raetsel);

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PNG_TEX);
		template = template.replace(PLACEHOLDER_LOESUNGSBUCHSTABE, "");
		template = template.replace(PLACEHOLDER_CONTENT, raetsel.getFrage());
		template = template.replace(PLACEHOLDER_ANTWORTEN, antworten);

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public String generateLoesungLaTeX(final Raetsel raetsel) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + "_l.tex";
		File file = new File(path);

		String textLoesungsbuchstabe = getTextLoesungsbuchstabe(raetsel.getAntwortvorschlaege());

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PNG_TEX);
		template = template.replace(PLACEHOLDER_LOESUNGSBUCHSTABE, textLoesungsbuchstabe);
		template = template.replace(PLACEHOLDER_CONTENT, raetsel.getLoesung());
		template = template.replace(PLACEHOLDER_ANTWORTEN, "");

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public String generateFrageUndLoesung(final Raetsel raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + "_x.tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(layoutAntwortvorschlaege)
			.generateLaTeXAntwortvorschlaege(raetsel);

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE_PDF_TEX);
		template = template.replace(PLACEHOLDER_CONTENT_FRAGE, raetsel.getFrage());
		template = template.replace(PLACEHOLDER_ANTWORTEN, antworten);

		if (StringUtils.isNotBlank(raetsel.getLoesung())) {

			String textLoesungsbuchstabe = getTextLoesungsbuchstabe(raetsel.getAntwortvorschlaege());
			template = template.replace(PLACEHOLDER_NEWPAGE, VALUE_NEWPAGE);
			template = template.replace(PLACEHOLDER_LOESUNGSBUCHSTABE, textLoesungsbuchstabe);
			template = template.replace(PLACEHOLDER_CONTENT_LOESUNG, raetsel.getLoesung());
		}

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public byte[] findImageFrage(final String schluessel) {

		String path = imagesBaseDir + File.separator + schluessel + ".png";
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

		String path = imagesBaseDir + File.separator + schluessel + "_l.png";
		return MjaFileUtils.loadBinaryFile(path, false);
	}

	@Override
	public byte[] findPDF(final String schluessel) {

		String path = imagesBaseDir + File.separator + schluessel + "_x.pdf";
		return MjaFileUtils.loadBinaryFile(path, true);

	}

	@Override
	public boolean existsGrafik(final String relativerPfad) {

		String path = imagesBaseDir + relativerPfad;
		File file = new File(path);

		return file.exists() && file.isFile();
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

	void setImagesBaseDir(final String imagesBaseDir) {

		this.imagesBaseDir = imagesBaseDir;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

}
