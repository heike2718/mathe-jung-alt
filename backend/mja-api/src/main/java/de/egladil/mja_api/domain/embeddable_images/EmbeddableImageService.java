// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageVorschau;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.impl.IncludegraphicsTextGenerator;
import de.egladil.mja_api.domain.upload.UploadedFile;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * EmbeddableImageService
 */
@ApplicationScoped
public class EmbeddableImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddableImageService.class);

	private final Pattern patternRelativePathForEps;

	private final IncludegraphicsTextGenerator includegraphicsTextGenerator;

	@Inject
	AuthenticationContext authCtx;

	@Inject
	EmbeddableImagesFilenameDelegate filenameDelegate;

	@Inject
	RaetselFileService fileService;

	@Inject
	ImageGeneratorService imageGeneratorService;

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	/**
	 *
	 */
	public EmbeddableImageService() {

		patternRelativePathForEps = Pattern.compile(MjaRegexps.REGEXP_RELATIVE_PATH_EPS_IN_TEXT);
		includegraphicsTextGenerator = new IncludegraphicsTextGenerator();

	}

	/**
	 * Generiert eine Vorschau der eingebetteten EmbeddableImageVorschau, falls sie existiert.
	 *
	 * @param  relativerPfad
	 * @return               EmbeddableImageVorschau oder null;
	 */
	public EmbeddableImageVorschau generatePreview(final String relativerPfad) {

		boolean exists = fileService.fileExists(relativerPfad);

		if (!exists) {

			return new EmbeddableImageVorschau()
				.withPfad(relativerPfad);
		}

		try {

			byte[] image = imageGeneratorService.generiereGrafikvorschau(relativerPfad);
			return new EmbeddableImageVorschau().withPfad(relativerPfad).withImage(image).markExists();
		} catch (Exception e) {

			LOGGER.error("Exception beim Laden des Images: " + e.getMessage(), e);
			return new EmbeddableImageVorschau()
				.withPfad(relativerPfad).markExists();
		}
	}

	/**
	 * Speichert die EmbeddableImageVorschau-Datei, sofern sie virenfrei ist und alle anderen Validierungen passen.
	 *
	 * @param  dto
	 * @return     MessagePayload
	 */
	public EmbeddableImageResponseDto replaceEmbeddedImage(@Valid final ReplaceEmbeddableImageRequestDto uploadRequestDto) {

		String relativerPfad = uploadRequestDto.getRelativerPfad();
		File file = new File(latexBaseDir + relativerPfad);

		if (!file.canRead()) {

			LOGGER.error("Zu ersetzende Datei nicht gefunden: pfad={}!", file.getAbsolutePath());

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("404 - Datei nicht gefunden")).build());
		}

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(uploadRequestDto.getFile().getDecodedData())) {

			IOUtils.copy(in, fos);
			fos.flush();
		} catch (IOException e) {

			LOGGER.error("Fehler beim Speichern im Filesystem: " + e.getMessage(), e);
			throw new MjaRuntimeException("Konnte Image nicht ins Filesystem speichern: " + e.getMessage(), e);
		}

		LOGGER.info("Grafikdatei hochgeladen: {} - {}", StringUtils.abbreviate(authCtx.getUser().getName(), 11),
			file.getAbsolutePath());

		String includegraphicsCommand = includegraphicsTextGenerator.generateIncludegraphicsText(relativerPfad);

		EmbeddableImageResponseDto result = new EmbeddableImageResponseDto().with(uploadRequestDto.getContext());
		result.setPfad(relativerPfad);
		result.setIncludegraphicsCommand(includegraphicsCommand);

		return result;
	}

	/**
	 * Das gegebene Image (ein eps) bekommt einen generierten Namen und wird in das passende Unterverzeichnis geschoben. Der String,
	 * mit dem das Image in LaTeX eingebettet wird, wird an den gegebenen Text angehängt. Es wird eine Grafig generiert und mit dem
	 * Response zurückgegeben, damit sie nach dem Hochladen direkt angezeigt werden kann.
	 *
	 * @param  uploadedFile
	 *                      UploadedFile
	 * @return              EmbeddableImageResponseDto
	 */
	public EmbeddableImageResponseDto createAndEmbedImage(final EmbeddableImageContext context, @Valid final UploadedFile uploadedFile) {

		String uuid = UUID.randomUUID().toString();
		String filenameUpload = uploadedFile.getName();

		String relativePath = filenameDelegate.getRelativePathForEmbeddableImage(uuid, filenameUpload);
		File file = new File(latexBaseDir + relativePath);

		File uploadDir = new File(file.getParent());

		if (!uploadDir.exists()) {

			uploadDir.mkdirs();
		}

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(uploadedFile.getDecodedData())) {

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
