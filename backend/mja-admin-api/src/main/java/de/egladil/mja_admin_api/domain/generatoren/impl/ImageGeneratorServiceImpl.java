// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import java.io.File;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_admin_api.domain.utils.MjaFileUtils;
import de.egladil.mja_admin_api.infrastructure.restclient.LaTeXRestClient;
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
		File file = new File(path);

		this.writeOutput(relativerPfad, file, template);

		Response response = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		response = laTeXClient.latex2PNG(filenameOhneSuffix);
		MessagePayload message = response.readEntity(MessagePayload.class);

		file.delete();

		if (message.isOk()) {

			String pathImage = path.replaceAll(".tex", ".png");
			byte[] image = MjaFileUtils.loadBinaryFile(pathImage, true);
			// System.out.println(new String(Base64.getEncoder().encode(image)));

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

		String errorMessage = "konnte kein LaTex-File schreiben Grafik: [relativerPfad=" + relativerPfad + "]";

		MjaFileUtils.writeOutput(file, template, errorMessage);
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

}