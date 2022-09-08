// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.grafiken.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaAdminServerException;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.grafiken.Grafik;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.raetsel.impl.FindPathsGrafikParser;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * GrafikServiceImpl
 */
@ApplicationScoped
public class GrafikServiceImpl implements GrafikService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrafikServiceImpl.class);

	private final Pattern pattern;

	@Inject
	RaetselFileService fileService;

	@Inject
	ImageGeneratorService imageGeneratorService;

	@ConfigProperty(name = "images.base.dir")
	String imagesBaseDir;

	/**
	 *
	 */
	public GrafikServiceImpl() {

		pattern = Pattern.compile(FindPathsGrafikParser.REGEXP_GRAFIK);

	}

	@Override
	public Grafik findGrafik(final String relativerPfad) {

		if (relativerPfad == null) {

			LOGGER.error("Aufruf ohne relativen Pfad");
			return new Grafik().withMessagePayload(MessagePayload.error("Aufruf ohne Pfad")).withPfad("");
		}

		if (!validPath(relativerPfad)) {

			LOGGER.error("Aufruf mit ungültigem relativen Pfad!");
			return new Grafik().withMessagePayload(MessagePayload.error("Aufruf mit ungültigem Pfad")).withPfad("");
		}

		boolean exists = fileService.existsGrafik(relativerPfad);

		if (!exists) {

			return new Grafik()
				.withMessagePayload(MessagePayload.warn("Falls der Pfad stimmt, wurde die Grafik noch nicht hochgeladen."))
				.withPfad(relativerPfad);
		}

		try {

			byte[] image = imageGeneratorService.generiereGrafikvorschau(relativerPfad);
			LOGGER.info("Grafikvorschau generiert: {}", relativerPfad);
			return new Grafik().withMessagePayload(MessagePayload.ok()).withPfad(relativerPfad).withImage(image);
		} catch (Exception e) {

			LOGGER.error("Exception beim Laden des Images: " + e.getMessage(), e);
			return new Grafik()
				.withMessagePayload(
					MessagePayload.warn("Die Grafik existiert zwar, aber beim Umwandeln in png lief etwas schief."))
				.withPfad(relativerPfad);
		}
	}

	@Override
	public MessagePayload grafikSpeichern(final String relativerPfad, final UploadRequestDto uploadRequestDto) {

		if (relativerPfad == null) {

			LOGGER.error("Aufruf ohne relativen Pfad");
			return MessagePayload.error("Aufruf ohne Pfad");
		}

		if (!validPath(relativerPfad)) {

			LOGGER.error("Aufruf mit ungültigem relativen Pfad!");
			return MessagePayload.error("Aufruf mit ungültigem Pfad");
		}

		File file = new File(imagesBaseDir + relativerPfad);

		File uploadDir = new File(file.getParent());

		if (!uploadDir.exists()) {

			uploadDir.mkdirs();
		}

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(uploadRequestDto.getUploadData().getData())) {

			IOUtils.copy(in, fos);
			fos.flush();
		} catch (IOException e) {

			LOGGER.error("Fehler beim Speichern im Filesystem: " + e.getMessage(), e);
			throw new MjaAdminServerException("Konnte Grafik nicht ins Filesystem speichern: " + e.getMessage(), e);
		}

		LOGGER.info("Grafikdatei hochgeladen: {} - {}", uploadRequestDto.getBenutzerUuid(), file.getAbsolutePath());
		return MessagePayload.info("Grafik erfolgreich gespeichert");
	}

	boolean validPath(final String relativerPfad) {

		Matcher matcher = pattern.matcher(relativerPfad);
		return matcher.matches();
	}

}
