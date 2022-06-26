// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_admin_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;
import de.egladil.web.mja_shared.exceptions.MjaRuntimeException;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final String PLACEHOLDER_ANTWORTEN = "{antwortvorschlaege}";

	private static final String PLACEHOLDER_LOESUNGSBUCHSTABE = "{loesungsbuchstabe}";

	private static final String PLACEHOLDER_CONTENT = "{content}";

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

		String template = loadTemplatePdf();
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

		String template = loadTemplatePdf();
		template = template.replace(PLACEHOLDER_LOESUNGSBUCHSTABE, textLoesungsbuchstabe);
		template = template.replace(PLACEHOLDER_CONTENT, raetsel.getLoesung());
		template = template.replace(PLACEHOLDER_ANTWORTEN, "");

		writeOutput(raetsel, file, template);
		LOGGER.debug("latex file generated: " + path);
		return path;
	}

	@Override
	public byte[] findImageFrage(final String schluessel) {

		String path = imagesBaseDir + File.separator + schluessel + ".png";
		File file = new File(path);

		if (file.exists() && file.isFile()) {

			try (FileInputStream in = new FileInputStream(file)) {

				return in.readAllBytes();

			} catch (IOException e) {

				LOGGER.warn("konnte {}.png nicht laden - wird ignoriert: {}", schluessel, e.getMessage(), e);
				return null;

			}
		}

		return null;
	}

	/**
	 * @param  raetsel
	 * @param  file
	 * @param  template
	 * @return          String
	 */
	private void writeOutput(final Raetsel raetsel, final File file, final String template) {

		try (Reader reader = new StringReader(template); FileOutputStream fos = new FileOutputStream(file)) {

			IOUtils.copy(reader, fos, Charset.forName("UTF-8"));
			fos.flush();

		} catch (IOException e) {

			String message = "konnte kein LaTex-File schreiben Raetsel: [schluessel=" + raetsel.getSchluessel()
				+ ", uuid="
				+ raetsel.getId() + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}
	}

	@Override
	public byte[] findImageLoesung(final String schluessel) {

		String path = imagesBaseDir + File.separator + schluessel + "_l.png";
		File file = new File(path);

		if (file.exists() && file.isFile()) {

			try (FileInputStream in = new FileInputStream(file)) {

				return in.readAllBytes();

			} catch (IOException e) {

				LOGGER.warn("konnte {}_l.png nicht laden - wird ignoriert: {}", schluessel, e.getMessage(), e);
				return null;

			}
		}

		return null;
	}

	String loadTemplatePdf() {

		try (InputStream in = getClass().getResourceAsStream("/latex/template-pdf.tex");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, Charset.forName("UTF-8"));

			return sw.toString();
		} catch (IOException e) {

			String message = "konnte das template nicht laden";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}

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
