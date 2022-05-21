// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselFileService;

/**
 * RaetselFileServiceImpl
 */
@ApplicationScoped
public class RaetselFileServiceImpl implements RaetselFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselFileServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Override
	public String generateLaTeXDocumentOfRaetselFrage(final Raetsel raetsel) {

		String path = latexBaseDir + File.separator + raetsel.getSchluessel() + ".tex";
		File file = new File(path);

		try (Reader reader = new StringReader(raetsel.getFrage()); FileOutputStream fos = new FileOutputStream(file)) {

			IOUtils.copy(reader, fos, Charset.forName("UTF-8"));
			fos.flush();

			return path;

		} catch (IOException e) {

			String message = "konnte kein LaTex-File generieren Raetsel: [schluessel=" + raetsel.getSchluessel() + ", uuid="
				+ raetsel.getId() + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

}
