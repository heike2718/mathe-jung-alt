// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
import de.egladil.mja_api.domain.upload.dto.Textart;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * FileUplodServiceTest
 */
@QuarkusTest
public class FileUplodServiceTest {

	/**
	 *
	 */
	private static final String RAETSEL_ID = "hksldha";

	private static final String RAETSELID_NEU = "neu";

	private AuthenticatedUser authenticatedUser;

	private UploadData uploadData;

	private PersistentesRaetsel raetsel;

	@Inject
	FileUplodService service;

	@InjectMock
	ProcessUploadService processUploadService;

	@InjectMock
	GrafikService grafikService;

	@InjectMock
	UploadScannerDelegate uploadScanner;

	@InjectMock
	AuthenticationContext authCtx;

	@InjectMock
	RaetselDao raetselDao;

	@BeforeEach
	void setUp() {

		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");

		byte[] data = MjaFileUtils.loadBinaryFile(file.getAbsolutePath(), false);

		uploadData = new UploadData("00000.eps", file);
		uploadData.setData(data);

		raetsel = new PersistentesRaetsel();
		raetsel.owner = "adhaiohq";

		authenticatedUser = new AuthenticatedUser("gdgagsa");
		authenticatedUser.withRoles(new String[] {});

	}

	@Nested
	class CreateImageTests {

		@Test
		void should_createEmbeddableImageThrowWebApplicationException403_when_raetselExistsAndUserAutorButNotOwner() {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
			when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);

			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.FRAGE);

			try {

				service.createEmbeddableImage(context, uploadData);
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx).isUserInRole("ADMIN");
				verify(authCtx).isUserInRole("AUTOR");

				verify(raetselDao).getWithID(RAETSEL_ID);

				verify(processUploadService, never()).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
			}
		}

		@Test
		void should_createEmbeddableImageThrowWebApplicationException403_when_userNeitherAutorNorAdmin() {

			// Arrange
			when(authCtx.getUser()).thenReturn(authenticatedUser);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			try {
				service.createEmbeddableImage(context, uploadData);
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());
				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao, never()).getWithID(anyString());
				verify(processUploadService, never()).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
			}
		}

		@Test
		void should_createEmbeddableImagePropagateMjaRuntimeException() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.getWithID(RAETSELID_NEU)).thenReturn(null);
			when(processUploadService.processFile(any(UploadData.class))).thenThrow(new MjaRuntimeException("gibet nicht"));

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			try {

				service.createEmbeddableImage(context, uploadData);
				fail("keine MjaRuntimeException");
			} catch (MjaRuntimeException e) {

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao).getWithID(anyString());

				verify(processUploadService).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
			}
		}

		@Test
		void should_createEmbeddableImageThrowMjaRuntimeException_when_VirusOderSo() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.getWithID(RAETSELID_NEU)).thenReturn(null);
			when(processUploadService.processFile(any(UploadData.class))).thenReturn(new Upload());

			doThrow(new UploadFormatException("Hilfe Virus one, one, one, ...")).when(uploadScanner)
				.scanUpload(any(UploadRequestDto.class));

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			try {

				service.createEmbeddableImage(context, uploadData);
				fail("keine MjaRuntimeException");
			} catch (MjaRuntimeException e) {

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao).getWithID(anyString());

				verify(processUploadService).processFile(any(UploadData.class));
				verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
			}

		}

		@Test
		void should_createEmbeddableImageWork_when_UserAutorAndRaetselNull() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.getWithID(RAETSELID_NEU)).thenReturn(null);
			when(processUploadService.processFile(any(UploadData.class))).thenReturn(new Upload());
			doNothing().when(uploadScanner)
				.scanUpload(any(UploadRequestDto.class));

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			EmbeddableImageResponseDto responseDto = new EmbeddableImageResponseDto().with(context);

			when(grafikService.createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class))).thenReturn(responseDto);

			// Act
			EmbeddableImageResponseDto result = service.createEmbeddableImage(context, uploadData);

			// Assert
			assertNotNull(result);

			verify(authCtx).getUser();
			verify(authCtx, never()).isUserInRole("ADMIN");
			verify(authCtx, never()).isUserInRole("AUTOR");

			verify(raetselDao).getWithID(anyString());

			verify(processUploadService).processFile(any(UploadData.class));
			verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
			verify(grafikService).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
		}

		@Test
		void should_createEmbeddableImageWork_when_UserAutorAndRaetselOwner() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			raetsel.owner = authenticatedUser.getUuid();

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);

			when(processUploadService.processFile(any(UploadData.class))).thenReturn(new Upload());
			doNothing().when(uploadScanner)
				.scanUpload(any(UploadRequestDto.class));

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			EmbeddableImageResponseDto responseDto = new EmbeddableImageResponseDto().with(context);

			when(grafikService.createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class))).thenReturn(responseDto);

			// Act
			EmbeddableImageResponseDto result = service.createEmbeddableImage(context, uploadData);

			// Assert
			assertNotNull(result);

			verify(authCtx).getUser();
			verify(authCtx, never()).isUserInRole("ADMIN");
			verify(authCtx, never()).isUserInRole("AUTOR");

			verify(raetselDao).getWithID(anyString());

			verify(processUploadService).processFile(any(UploadData.class));
			verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
			verify(grafikService).createAndEmbedImage(any(EmbeddableImageContext.class), any(Upload.class));
		}
	}

	@Nested
	class ReplaceImageTests {

		private static final String RELATIVER_PFAD = "/resources/001/00000.eps";

	@Test
		void should_replaceTheEmbeddableImageThrowWebApplicationException403_when_userAutorButNotOwner() throws Exception {

			// Arrange
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
			when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);
			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);

			// Act
			try {

				service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(processUploadService, never()).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).replaceEmbeddedImage(any(UploadRequestDto.class));

			}

		}

	@Test
		void should_replaceTheEmbeddableImageThrowWebApplicationException403_when_userNeitherAutorNorAdmin() throws Exception {

			// Arrange
			when(authCtx.getUser()).thenReturn(authenticatedUser);

			// Act
			try {

				service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao, never()).getWithID(anyString());
				verify(processUploadService, never()).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).replaceEmbeddedImage(any(UploadRequestDto.class));

			}

		}

		@Test
		void should_replaceTheEmbeddableImageThrowWebApplicationException404_when_raetselNotFound() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });

			when(authCtx.getUser()).thenReturn(authenticatedUser);

			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(null);

			// Act
			try {

				service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(404, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(processUploadService, never()).processFile(any(UploadData.class));
				verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
				verify(grafikService, never()).replaceEmbeddedImage(any(UploadRequestDto.class));

			}

		}

		@Test
		void should_replaceTheEmbeddableImageReturnError_when_MjaRuntimeException() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.TRUE);
			when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.FALSE);
			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);
			when(processUploadService.processFile(any(UploadData.class))).thenThrow(new MjaRuntimeException("gibet nicht"));

			// Act
			MessagePayload result = service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
			assertEquals("Es ist ein Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de.", result.getMessage());
			assertEquals("ERROR", result.getLevel());

			verify(authCtx).getUser();
			verify(authCtx).isUserInRole("ADMIN");
			verify(authCtx).isUserInRole("AUTOR");
			verify(processUploadService).processFile(any(UploadData.class));
			verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
			verify(grafikService, never()).replaceEmbeddedImage(any(UploadRequestDto.class));

		}

		@Test
		void should_replaceTheEmbeddableImageReturnError_when_UploadFormatException() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.TRUE);
			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);
			when(processUploadService.processFile(any(UploadData.class))).thenReturn(new Upload());

			doThrow(new UploadFormatException("Hilfe Virus one, one, one, ...")).when(uploadScanner)
				.scanUpload(any(UploadRequestDto.class));

			// Act
			MessagePayload result = service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
			assertEquals("Die Datei ist nicht akzeptabel.", result.getMessage());
			assertEquals("ERROR", result.getLevel());

			verify(authCtx).getUser();
			verify(authCtx).isUserInRole("ADMIN");
			verify(authCtx).isUserInRole("AUTOR");
			verify(processUploadService).processFile(any(UploadData.class));
			verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
			verify(grafikService, never()).replaceEmbeddedImage(any(UploadRequestDto.class));
		}

		@Test
		void should_replaceTheEmbeddableImageReturnSuccess_when_UserAutorAndOwner() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			raetsel.owner = authenticatedUser.getUuid();

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
			when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);
			when(raetselDao.getWithID(RAETSEL_ID)).thenReturn(raetsel);
			when(processUploadService.processFile(any(UploadData.class))).thenReturn(new Upload());

			doNothing().when(uploadScanner)
				.scanUpload(any(UploadRequestDto.class));

			MessagePayload mp = MessagePayload.ok();
			when(grafikService.replaceEmbeddedImage(any(UploadRequestDto.class))).thenReturn(mp);

			// Act
			MessagePayload result = service.replaceTheEmbeddableImage(RAETSEL_ID, uploadData, RELATIVER_PFAD);
			assertEquals("ok", result.getMessage());
			assertEquals("INFO", result.getLevel());

			verify(authCtx).getUser();
			verify(authCtx).isUserInRole("ADMIN");
			verify(authCtx).isUserInRole("AUTOR");
			verify(raetselDao).getWithID(RAETSEL_ID);
			verify(processUploadService).processFile(any(UploadData.class));
			verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
			verify(grafikService).replaceEmbeddedImage(any(UploadRequestDto.class));
		}
	}
}
