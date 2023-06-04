// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * ImageGeneratorServiceImpl
 */
@ApplicationScoped
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

	private static final String PLACEHOLDER_PFAD = "{pfad}";

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageGeneratorServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Override
	public byte[] generiereGrafikvorschau(final String relativerPfad) {

		String template = MjaFileUtils.loadTemplate("/latex/template-grafik.tex");
		template = template.replace(PLACEHOLDER_PFAD, relativerPfad);

		String filenameOhneSuffix = UUID.randomUUID().toString();
		String path = latexBaseDir + File.separator + filenameOhneSuffix + ".tex";
		String pathImage = latexBaseDir + File.separator + filenameOhneSuffix + ".png";

		LOGGER.debug("===> relativerPfad={}, path={}, pathImage={}", relativerPfad, path, pathImage);
		File file = new File(path);

		this.writeOutput(relativerPfad, file, template);

		Response response = null;

		response = laTeXClient.latex2PNG(filenameOhneSuffix);
		MessagePayload message = response.readEntity(MessagePayload.class);
		file.delete();

		if (message.isOk()) {

			byte[] image = MjaFileUtils.loadBinaryFile(pathImage, true);

			if (image == null) {

				LOGGER.warn("{}: image ist null", relativerPfad);
			} else {

				LOGGER.info("Grafikvorschau generiert: {}", relativerPfad);
			}

			return image;
		}
		LOGGER.error(message.getMessage());
		return null;
	}

	/**
	 * @param  raetsel
	 * @param  file
	 * @param  template
	 * @return          String
	 */
	private void writeOutput(final String relativerPfad, final File file, final String template) {

		LOGGER.debug("relativerPfad={}, file={}", relativerPfad, file.getAbsolutePath());

		String errorMessage = "konnte kein LaTex-File schreiben Grafik: [relativerPfad=" + relativerPfad + "]";

		MjaFileUtils.writeOutput(file, template, errorMessage);
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

}
