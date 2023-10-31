// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

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

	@Inject
	AuthenticationContext authCtx;

	@Inject
	RaetselDao raetselDao;

	/**
	 * Wenn mit den UploadData alles palletti ist, werden ein Dateiname generiert, die Datei in ein entsprechendes Verzeichnis
	 * gespeichert, der LaTeX-includegraphics-Befehl an den text im requestDto angehängt und ein Vorschaubild generiert.
	 *
	 * @param  uploadContext
	 *                       EmbeddableImageContext kontext zum Upload. Zurückzureichen an Client
	 * @param  uploadData
	 *                       UploadData - das, was Quarkus beim FileUpload produziert.
	 * @return               EmbeddableImageResponseDto
	 */
	public EmbeddableImageResponseDto createEmbeddableImage(final EmbeddableImageContext uploadContext, final UploadData uploadData) {

		AuthenticatedUser user = authCtx.getUser();
		String userId = user.getUuid();

		if (PermissionUtils.isUserOrdinary(user.getRoles())) {

			LOGGER.warn("User {} hat versucht, eine Grafik hochzuladen", userId);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		PersistentesRaetsel raetsel = raetselDao.getWithID(uploadContext.getRaetselId());

		if (raetsel != null && !PermissionUtils.hasWritePermission(userId,
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), raetsel.owner)) {

			LOGGER.warn("User {} hat versucht, zu Raetsel {} mit Owner {} eine Grafik hochzuladen", userId, raetsel.schluessel,
				raetsel.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		try {

			Upload upload = processUploadService.processFile(uploadData);

			uploadScanner.scanUpload(new UploadRequestDto()
				.withBenutzerUuid(user.getName())
				.withUpload(upload));

			return grafikService.createAndEmbedImage(uploadContext, upload);

		} catch (MjaRuntimeException e) {

			LOGGER.error(e.getMessage(), e);

			throw e;
		} catch (UploadFormatException e) {
			// wurde schon geloggt

			throw new MjaRuntimeException("upload enthält viren oder anderes zeug");

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			String errorMessage = applicationMessages.getString("general.internalServerError");
			throw new MjaRuntimeException(errorMessage);

		}
	}

	/**
	 * @param  raetselId
	 * @param  uploadData
	 * @param  relativerPfad
	 * @return               MessagePayload
	 */
	public MessagePayload replaceTheEmbeddableImage(final String raetselId, final UploadData uploadData, final String relativerPfad) {

		AuthenticatedUser user = authCtx.getUser();
		String userId = user.getUuid();

		if (PermissionUtils.isUserOrdinary(user.getRoles())) {

			LOGGER.warn("User {} hat versucht, eine Grafik hochzuladen", userId);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		PersistentesRaetsel raetsel = raetselDao.getWithID(raetselId);

		if (raetsel == null) {

			LOGGER.error(
				"User hat versucht, eine Grafik zu einem nicht existierenden Rätsel hochzuladen: raetselUUID={}, userUUID={}",
				raetselId, StringUtils.abbreviate(userId, 8));

			throw new WebApplicationException(Status.NOT_FOUND);
		}

		if (!PermissionUtils.hasWritePermission(userId,
			PermissionUtils.getRolesWithWriteRaetselAndRaetselgruppenPermission(authCtx), raetsel.owner)) {

			LOGGER.warn("User {} hat versucht, zu Raetsel {} mit Owner {} eine Grafik hochzuladen", userId, raetsel.schluessel,
				raetsel.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		try {

			Upload upload = processUploadService.processFile(uploadData);

			UploadRequestDto uploadRequest = new UploadRequestDto()
				.withBenutzerUuid(user.getName())
				.withUpload(upload).withRelativerPfad(relativerPfad);

			uploadScanner.scanUpload(uploadRequest);

			return grafikService.replaceEmbeddedImage(uploadRequest);

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
