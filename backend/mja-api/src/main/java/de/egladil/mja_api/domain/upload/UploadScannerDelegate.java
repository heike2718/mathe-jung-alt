// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.restclient.FilescannerRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * UploadScannerDelegate
 */
@ApplicationScoped
public class UploadScannerDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadScannerDelegate.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	AuthenticationContext authCtx;

	@ConfigProperty(name = "upload.max.bytes")
	String maxFilesizeBytes;

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@RestClient
	@Inject
	FilescannerRestClient fileScannerClient;

	public void scanUpload(final ReplaceEmbeddableImageRequestDto uploadPayload, final List<FileType> acceptableFileTypes) throws UploadFormatException {

		String fileOwnerId = authCtx.getUser().getName();
		int maxBytes = Integer.valueOf(maxFilesizeBytes);

		UploadedFile uploadedFile = uploadPayload.getFile();
		int size = uploadedFile.getDecodedData().length;

		if (size > maxBytes) {

			String errorMessage = applicationMessages.getString("upload.maxSizeExceeded");
			LOGGER.warn(errorMessage);
			throw new UploadFormatException(errorMessage);
		}

		ScanRequestPayload scanRequestPayload = new ScanRequestPayload().withClientId(clientId)
			.withFileOwner(fileOwnerId).withUpload(uploadedFile);

		Response response = fileScannerClient.scanUpload(scanRequestPayload);

		if (response.getStatus() != 200) {

			String message = "Beim Scannen des Uploads ist ein Fehler aufgetreten: filescanner response not ok: status="
				+ response.getStatus();
			LOGGER.error(message);
			throw new MjaRuntimeException(message);
		}

		FileScanResult scanResult = response.readEntity(FileScanResult.class);

		VirusDetection virusDetection = scanResult.getVirusDetection();

		if (virusDetection != null && virusDetection.isVirusDetected()) {

			String scannerMessage = virusDetection.getScannerMessage();
			LOGGER.warn(scannerMessage);
			throw new UploadFormatException(applicationMessages.getString("upload.dangerousContent"));

		}

		ThreadDetection threadDetection = scanResult.getThreadDetection();

		if (threadDetection != null && threadDetection.isSecurityThreadDetected()) {

			String securityCheckMessage = threadDetection.getSecurityCheckMessage();
			LOGGER.warn(securityCheckMessage);
			throw new UploadFormatException(applicationMessages.getString("upload.dangerousContent"));
		}

		DateiTyp dateiTyp = DateiTyp.valueOfTikaName(scanResult.getMediaType());

		if (dateiTyp == null) {

			LOGGER.error("Unbekannter MediaType {} - brechen ab.", scanResult.getMediaType());
			throw new UploadFormatException(applicationMessages.getString("upload.unbekannterMediaType"));
		}

		final FileType actualFileType = dateiTyp.getFileType();

		Optional<FileType> optFilteredType = acceptableFileTypes.stream().filter(t -> actualFileType == t).findFirst();

		if (optFilteredType.isEmpty()) {

			LOGGER.error("FileType {} ist nicht erlaubt: erlaubt isr {} - brechen ab.", actualFileType,
				fileTypesToString(acceptableFileTypes));
			throw new UploadFormatException(applicationMessages.getString("upload.badFiletype"));
		}
	}

	String fileTypesToString(final List<FileType> acceptableFileTypes) {

		return acceptableFileTypes.stream()
			.map(n -> n.toString())
			.collect(Collectors.joining("-", "{", "}"));
	}
}
