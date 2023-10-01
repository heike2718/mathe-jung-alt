// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
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

	@ConfigProperty(name = "upload.max.bytes")
	String maxFilesizeBytes;

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@RestClient
	@Inject
	FilescannerRestClient fileScannerClient;

	public FileScanResult scanUpload(final UploadRequestDto uploadPayload) throws UploadFormatException {

		String fileOwnerId = uploadPayload.getBenutzerUuid();
		int maxBytes = Integer.valueOf(maxFilesizeBytes);

		int size = uploadPayload.getUploadData().size();

		if (size > maxBytes) {

			String errorMessage = applicationMessages.getString("upload.maxSizeExceeded");
			LOGGER.warn(errorMessage);
			throw new UploadFormatException(errorMessage);
		}

		Upload upload = uploadPayload.getUploadData().toUpload();
		ScanRequestPayload scanRequestPayload = new ScanRequestPayload().withClientId(clientId)
			.withFileOwner(fileOwnerId).withUpload(upload);

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

		return scanResult;
	}
}
