// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import java.io.File;
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

import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselFileService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.AnzeigeAntwortvorschlaegeTyp;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final String PLACEHOLDER_ANTWORTEN = "{antwortvorschlaege}";

	private static final String PLACEHOLDER_CONTENT = "{content}";

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Override
	public String generateFrageLaTeX(final Raetsel raetsel, final Outputformat outputformat, final AnzeigeAntwortvorschlaegeTyp anzeigeAntwortenTyp) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + ".tex";
		File file = new File(path);

		String antworten = AntwortvorschlagGeneratorStrategegy.create(anzeigeAntwortenTyp).generateLaTeXAntwortvorschlaege(raetsel);

		String template = loadTemplatePdf();
		template = template.replace(PLACEHOLDER_CONTENT, raetsel.getFrage());
		template = template.replace(PLACEHOLDER_ANTWORTEN, antworten);

		try (Reader reader = new StringReader(template); FileOutputStream fos = new FileOutputStream(file)) {

			IOUtils.copy(reader, fos, Charset.forName("UTF-8"));
			fos.flush();

			LOGGER.info("latex file generated: " + path);

			return path;

		} catch (IOException e) {

			String message = "konnte kein LaTex-File generieren Raetsel: [schluessel=" + raetsel.getSchluessel()
				+ ", uuid="
				+ raetsel.getId() + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}
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

}
