// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.domain.grafiken.GrafikService;

/**
 * FileUplodService
 */
@ApplicationScoped
public class FileUplodService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUplodService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	ProcessUploadService processUploadService;

	@Inject
	GrafikService grafikService;

	@Inject
	UploadScannerDelegate uploadScanner;

	@Context
	SecurityContext securityContext;

	public MessagePayload saveTheUpload(final UploadData uploadData, final String relativerPfad) {

		try {

			processUploadService.processFile(uploadData);

			UploadRequestDto uploadRequest = new UploadRequestDto()
				.withBenutzerUuid(securityContext.getUserPrincipal().getName())
				.withUploadData(uploadData).withRelativerPfad(relativerPfad);

			uploadScanner.scanUpload(uploadRequest);

			return grafikService.grafikSpeichern(uploadRequest);

		} catch (UploadFormatException e) {
			// wurde schon geloggt

			return MessagePayload.error("Die Datei ist nicht akzeptabel.");

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			String errorMessage = applicationMessages.getString("general.internalServerError");
			return MessagePayload.error(errorMessage);

		}
	}
}
