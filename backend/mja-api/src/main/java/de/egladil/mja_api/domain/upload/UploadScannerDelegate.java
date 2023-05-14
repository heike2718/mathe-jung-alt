// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.web.filescanner_service.clamav.VirusDetection;
import de.egladil.web.filescanner_service.scan.ScanRequestPayload;
import de.egladil.web.filescanner_service.scan.ScanResult;
import de.egladil.web.filescanner_service.scan.ScanService;
import de.egladil.web.filescanner_service.securitychecks.ThreadDetection;

/**
 * UploadScannerDelegate
 */
@ApplicationScoped
public class UploadScannerDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadScannerDelegate.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@ConfigProperty(name = "upload.max.bytes")
	String maxFilesizeBytes;

	@ConfigProperty(name = "admin-client-id")
	String clientId;

	@Inject
	ScanService scanService;

	public ScanResult scanUpload(final UploadRequestDto uploadPayload) throws UploadFormatException {

		String fileOwnerId = uploadPayload.getBenutzerUuid();
		int maxBytes = Integer.valueOf(maxFilesizeBytes);

		int size = uploadPayload.getUploadData().size();

		if (size > maxBytes) {

			String errorMessage = applicationMessages.getString("upload.maxSizeExceeded");
			LOGGER.warn(errorMessage);
			throw new UploadFormatException(errorMessage);
		}

		ScanRequestPayload scanRequestPayload = new ScanRequestPayload().withClientId(clientId)
			.withFileOwner(fileOwnerId).withUpload(uploadPayload.getUploadData().toUpload());

		ScanResult scanResult = scanService.scanFile(scanRequestPayload);

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
