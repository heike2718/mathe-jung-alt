// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.upload;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;
import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.domain.embeddable_images.EmbeddableImageService;
import de.egladil.raetselbaukasten.domain.embeddable_images.dto.CreateEmbeddableImageRequestDto;
import de.egladil.raetselbaukasten.domain.embeddable_images.dto.EmbeddableImageContext;
import de.egladil.raetselbaukasten.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.raetselbaukasten.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.exceptions.UploadFormatException;
import de.egladil.raetselbaukasten.domain.raetsel.impl.RaetselPermissionDelegate;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.RaetselDao;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesRaetsel;

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
    RaetselPermissionDelegate permissionDelegate;

    @Inject
    RaetselDao raetselDao;

    /**
     * Wenn mit den UploadData alles palletti ist, werden ein Dateiname generiert,
     * die Datei in ein entsprechendes Verzeichnis gespeichert, der
     * LaTeX-includegraphics-Befehl an den text im requestDto angehängt und ein
     * Vorschaubild generiert.
     *
     * @param requestDto CreateEmbeddableImageRequestDto
     * @return EmbeddableImageResponseDto
     */
    @SuppressWarnings("resource") // ist false positive, weil der Container die Response schließt.
    public EmbeddableImageResponseDto createEmbeddableImage(final CreateEmbeddableImageRequestDto requestDto) {

        AuthenticatedUser user = authCtx.getUser();
        String userId = user.getUuid();

        Benutzerart benutzerart = user.getBenutzerart();

        if (Benutzerart.ANONYM == benutzerart || Benutzerart.STANDARD == benutzerart) {

            LOGGER.warn("User {} hat versucht, eine EmbeddableImageVorschau hochzuladen", userId);

            throw new WebApplicationException(Status.FORBIDDEN);
        }

        EmbeddableImageContext uploadContext = requestDto.getContext();

        PersistentesRaetsel raetsel = raetselDao.findById(uploadContext.getRaetselId());

        if (raetsel != null) {

            // wirft eine WebApplicationException
            permissionDelegate.checkWritePermission(raetsel);
        }

        try {

            UploadedFile upload = requestDto.getFile();

            uploadScanner.scanUpload(new ReplaceEmbeddableImageRequestDto().withUpload(upload), ACCEPTABLE_FILETYPES);

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
     * @param raetselId
     * @param uploadData
     * @param relativerPfad
     * @return MessagePayload
     */
    @SuppressWarnings("resource") // ist false positive, weil der Container die Response schließt.
    public EmbeddableImageResponseDto replaceTheEmbeddableImage(final ReplaceEmbeddableImageRequestDto requestDto) {

        AuthenticatedUser user = authCtx.getUser();
        String userId = user.getUuid();

        Benutzerart benutzerart = user.getBenutzerart();

        if (Benutzerart.ANONYM == benutzerart || Benutzerart.STANDARD == benutzerart) {

            LOGGER.warn("User {} hat versucht, eine EmbeddableImageVorschau hochzuladen", userId);

            throw new WebApplicationException(Status.FORBIDDEN);
        }

        String raetselId = requestDto.getContext().getRaetselId();

        PersistentesRaetsel raetsel = raetselDao.findById(raetselId);

        if (raetsel == null) {

            LOGGER
                    .error("User hat versucht, eine EmbeddableImageVorschau zu einem nicht existierenden Rätsel hochzuladen: raetselUUID={}, userUUID={}",
                            raetselId, StringUtils.abbreviate(userId, 8));

            throw new WebApplicationException(Status.NOT_FOUND);
        }

        permissionDelegate.checkWritePermission(raetsel);

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
