// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.grafiken.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.impl.IncludegraphicsTextGenerator;
import de.egladil.mja_api.domain.grafiken.Grafik;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.upload.Upload;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * GrafikServiceImpl
 */
@ApplicationScoped
public class GrafikServiceImpl implements GrafikService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrafikServiceImpl.class);

	private final Pattern patternRelativePathForEps;

	private final IncludegraphicsTextGenerator includegraphicsTextGenerator;

	@Inject
	FilenameDelegate filenameDelegate;

	@Inject
	RaetselFileService fileService;

	@Inject
	ImageGeneratorService imageGeneratorService;

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	/**
	 *
	 */
	public GrafikServiceImpl() {

		patternRelativePathForEps = Pattern.compile(MjaRegexps.REGEXP_RELATIVE_PATH_EPS_IN_TEXT);
		includegraphicsTextGenerator = new IncludegraphicsTextGenerator();

	}

	@Override
	public Grafik generatePreview(final String relativerPfad) {

		if (relativerPfad == null) {

			LOGGER.error("Aufruf ohne relativen Pfad");
			return new Grafik().withMessagePayload(MessagePayload.error("Aufruf ohne Pfad")).withPfad("");
		}

		if (!validPath(relativerPfad)) {

			LOGGER.error("Aufruf mit ungültigem relativen Pfad!");
			return new Grafik().withMessagePayload(MessagePayload.error("Aufruf mit ungültigem Pfad")).withPfad(relativerPfad);
		}

		boolean exists = fileService.existsGrafik(relativerPfad);

		if (!exists) {

			return new Grafik()
				.withMessagePayload(MessagePayload.warn("Falls der Pfad stimmt, wurde die Grafik noch nicht hochgeladen."))
				.withPfad(relativerPfad);
		}

		try {

			byte[] image = imageGeneratorService.generiereGrafikvorschau(relativerPfad);
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
	public MessagePayload replaceEmbeddedImage(final UploadRequestDto uploadRequestDto) {

		if (uploadRequestDto.getRelativerPfad() == null) {

			LOGGER.error("Aufruf ohne relativen Pfad");
			return MessagePayload.error("Aufruf ohne Pfad");
		}

		if (!validPath(uploadRequestDto.getRelativerPfad())) {

			LOGGER.error("Aufruf mit ungültigem relativen Pfad!");
			return MessagePayload.error("Aufruf mit ungültigem Pfad");
		}

		File file = new File(latexBaseDir + uploadRequestDto.getRelativerPfad());

		File uploadDir = new File(file.getParent());

		if (!uploadDir.exists()) {

			uploadDir.mkdirs();
		}

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(uploadRequestDto.getUpload().getDecodedData())) {

			IOUtils.copy(in, fos);
			fos.flush();
		} catch (IOException e) {

			LOGGER.error("Fehler beim Speichern im Filesystem: " + e.getMessage(), e);
			throw new MjaRuntimeException("Konnte Grafik nicht ins Filesystem speichern: " + e.getMessage(), e);
		}

		LOGGER.info("Grafikdatei hochgeladen: {} - {}", uploadRequestDto.getBenutzerUuid(), file.getAbsolutePath());
		return MessagePayload.info("Grafik erfolgreich gespeichert");
	}

	@Override
	public EmbeddableImageResponseDto createAndEmbedImage(final EmbeddableImageContext context, final Upload upload) {

		String uuid = UUID.randomUUID().toString();
		String filenameUpload = upload.getName();

		String relativePath = filenameDelegate.getRelativePathForEmbeddableImage(uuid, filenameUpload);
		File file = new File(latexBaseDir + relativePath);

		File uploadDir = new File(file.getParent());

		if (!uploadDir.exists()) {

			uploadDir.mkdirs();
		}

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(upload.getDecodedData())) {

			IOUtils.copy(in, fos);
			fos.flush();

		} catch (IOException e) {

			LOGGER.error("Fehler beim Speichern im Filesystem: " + e.getMessage(), e);
			throw new MjaRuntimeException("Fehler beim speichern des embeddable image: " + e.getMessage(), e);
		}

		String includegraphicsCommand = includegraphicsTextGenerator.generateIncludegraphicsText(relativePath);

		EmbeddableImageResponseDto result = new EmbeddableImageResponseDto().with(context);
		result.setPfad(relativePath);
		result.setIncludegraphicsCommand(includegraphicsCommand);

		return result;
	}

	boolean validPath(final String relativerPfad) {

		Matcher matcher = patternRelativePathForEps.matcher(relativerPfad);
		return matcher.matches();
	}
}
