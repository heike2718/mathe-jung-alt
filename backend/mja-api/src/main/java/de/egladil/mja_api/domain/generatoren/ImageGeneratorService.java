// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.io.File;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * ImageGeneratorService
 */
@ApplicationScoped
public class ImageGeneratorService {

	private static final String PLACEHOLDER_PFAD = "{pfad}";

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageGeneratorService.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	/**
	 * Erzeugt eine LaTeX-Datei mit dem Image, compiliert sie zu einem png und gibt das png als byte-Array zurück.
	 *
	 * @param  relativerPfad
	 *                       String
	 * @return               byte[] ein png
	 */
	public byte[] generiereGrafikvorschau(final String relativerPfad) {

		String template = MjaFileUtils.loadTemplate("/latex/template-grafik.tex");
		template = template.replace(PLACEHOLDER_PFAD, relativerPfad);

		String filenameOhneSuffix = UUID.randomUUID().toString();
		String path = latexBaseDir + File.separator + filenameOhneSuffix + ".tex";
		String pathImage = latexBaseDir + File.separator + filenameOhneSuffix + Outputformat.PNG.getFilenameExtension();

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

		String errorMessage = "konnte kein LaTex-File schreiben EmbeddableImageVorschau: [relativerPfad=" + relativerPfad + "]";

		MjaFileUtils.writeTextfile(file, template, errorMessage);
	}

	void setLatexBaseDir(final String latexBaseDir) {

		this.latexBaseDir = latexBaseDir;
	}

}
