// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.embeddable_images.EmbeddableImageService;
import de.egladil.mja_api.domain.embeddable_images.dto.CreateEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * EmbeddableImageUplodService
 */
@ApplicationScoped
public class EmbeddableImageUplodService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddableImageUplodService.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	final List<FileType> ACCEPTABLE_FILETYPES = Arrays.asList(new FileType[] { FileType.EPS });

	@Inject
	EmbeddableImageService embeddableImageService;

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
	 * @param  requestDto
	 *                    CreateEmbeddableImageRequestDto
	 * @return            EmbeddableImageResponseDto
	 */
	public EmbeddableImageResponseDto createEmbeddableImage(final CreateEmbeddableImageRequestDto requestDto) {

		AuthenticatedUser user = authCtx.getUser();
		String userId = user.getUuid();

		if (PermissionUtils.isUserOrdinary(user.getRoles())) {

			LOGGER.warn("User {} hat versucht, eine EmbeddableImageVorschau hochzuladen", userId);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		EmbeddableImageContext uploadContext = requestDto.getContext();

		PersistentesRaetsel raetsel = raetselDao.findById(uploadContext.getRaetselId());

		if (raetsel != null && !PermissionUtils.hasWritePermission(userId,
			PermissionUtils.getRolesWithWriteRaetselAndAufgabensammlungenPermission(authCtx), raetsel.owner)) {

			LOGGER.warn("User {} hat versucht, zu Raetsel {} mit Owner {} eine EmbeddableImageVorschau hochzuladen", userId,
				raetsel.schluessel,
				raetsel.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		try {

			UploadedFile upload = requestDto.getFile();

			uploadScanner.scanUpload(new ReplaceEmbeddableImageRequestDto()
				.withUpload(upload), ACCEPTABLE_FILETYPES);

			return embeddableImageService.createAndEmbedImage(uploadContext, upload);

		} catch (UploadFormatException e) {
			// wurde schon geloggt

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error(e.getMessage())).build());

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
	public EmbeddableImageResponseDto replaceTheEmbeddableImage(final ReplaceEmbeddableImageRequestDto requestDto) {

		AuthenticatedUser user = authCtx.getUser();
		String userId = user.getUuid();

		if (PermissionUtils.isUserOrdinary(user.getRoles())) {

			LOGGER.warn("User {} hat versucht, eine EmbeddableImageVorschau hochzuladen", userId);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		String raetselId = requestDto.getContext().getRaetselId();

		PersistentesRaetsel raetsel = raetselDao.findById(raetselId);

		if (raetsel == null) {

			LOGGER.error(
				"User hat versucht, eine EmbeddableImageVorschau zu einem nicht existierenden Rätsel hochzuladen: raetselUUID={}, userUUID={}",
				raetselId, StringUtils.abbreviate(userId, 8));

			throw new WebApplicationException(Status.NOT_FOUND);
		}

		if (!PermissionUtils.hasWritePermission(userId,
			PermissionUtils.getRolesWithWriteRaetselAndAufgabensammlungenPermission(authCtx), raetsel.owner)) {

			LOGGER.warn("User {} hat versucht, zu Raetsel {} mit Owner {} eine EmbeddableImageVorschau hochzuladen", userId,
				raetsel.schluessel,
				raetsel.owner);

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		try {

			uploadScanner.scanUpload(requestDto, ACCEPTABLE_FILETYPES);

			return embeddableImageService.replaceEmbeddedImage(requestDto);

		} catch (UploadFormatException e) {
			// wurde schon geloggt

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error(e.getMessage())).build());

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			String errorMessage = applicationMessages.getString("general.internalServerError");
			throw new MjaRuntimeException(errorMessage);

		}
	}

}
