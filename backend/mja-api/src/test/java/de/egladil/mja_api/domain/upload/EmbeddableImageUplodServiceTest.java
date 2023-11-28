// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.TestFileUtils;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.embeddable_images.EmbeddableImageService;
import de.egladil.mja_api.domain.embeddable_images.dto.CreateEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.Textart;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * EmbeddableImageUplodServiceTest
 */
@QuarkusTest
public class EmbeddableImageUplodServiceTest {

	private static final String RAETSEL_ID = "hksldha";

	private static final String RAETSELID_NEU = "neu";

	private AuthenticatedUser authenticatedUser;

	private UploadedFile uploadedFile;

	private PersistentesRaetsel raetsel;

	@Inject
	EmbeddableImageUplodService service;

	@InjectMock
	EmbeddableImageService embeddableImageService;

	@InjectMock
	UploadScannerDelegate uploadScanner;

	@InjectMock
	AuthenticationContext authCtx;

	@InjectMock
	RaetselDao raetselDao;

	@BeforeEach
	void setUp() throws Exception {

		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");

		uploadedFile = new UploadedFile().withName("00000.eps").withData(data);

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

			when(raetselDao.findById(RAETSEL_ID)).thenReturn(raetsel);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.FRAGE);

			CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
			requestDto.setContext(context);
			requestDto.setFile(uploadedFile);

			try {

				service.createEmbeddableImage(requestDto);
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx).isUserInRole("ADMIN");
				verify(authCtx).isUserInRole("AUTOR");

				verify(raetselDao).findById(RAETSEL_ID);

				verify(uploadScanner, never()).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).createAndEmbedImage(any(EmbeddableImageContext.class),
					any(UploadedFile.class));
			}
		}

		@Test
		void should_createEmbeddableImageThrowWebApplicationException403_when_userNeitherAutorNorAdmin() {

			// Arrange
			when(authCtx.getUser()).thenReturn(authenticatedUser);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
			requestDto.setContext(context);
			requestDto.setFile(uploadedFile);

			try {
				service.createEmbeddableImage(requestDto);
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());
				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao, never()).findById(anyString());
				verify(uploadScanner, never()).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).createAndEmbedImage(any(EmbeddableImageContext.class), any(UploadedFile.class));
			}
		}

		@Test
		void should_createEmbeddableImageThrowWebApplicationException_when_VirusOderSo() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.findById(RAETSELID_NEU)).thenReturn(null);

			doThrow(new UploadFormatException("Hilfe Virus one, one, one, ...")).when(uploadScanner)
				.scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);

			CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
			requestDto.setContext(context);
			requestDto.setFile(uploadedFile);

			try {

				service.createEmbeddableImage(requestDto);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao).findById(anyString());

				verify(uploadScanner).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).createAndEmbedImage(any(EmbeddableImageContext.class),
					any(UploadedFile.class));

				assertEquals(400, e.getResponse().getStatus());
			}

		}

		@Test
		void should_createEmbeddableImageWork_when_UserAutorAndRaetselNull() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.findById(RAETSELID_NEU)).thenReturn(null);
			doNothing().when(uploadScanner)
				.scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);
			CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
			requestDto.setContext(context);
			requestDto.setFile(uploadedFile);

			EmbeddableImageResponseDto responseDto = new EmbeddableImageResponseDto().with(context);

			when(embeddableImageService.createAndEmbedImage(any(EmbeddableImageContext.class), any(UploadedFile.class)))
				.thenReturn(responseDto);

			// Act
			EmbeddableImageResponseDto result = service.createEmbeddableImage(requestDto);

			// Assert
			assertNotNull(result);

			verify(authCtx).getUser();
			verify(authCtx, never()).isUserInRole("ADMIN");
			verify(authCtx, never()).isUserInRole("AUTOR");

			verify(raetselDao).findById(anyString());

			verify(uploadScanner).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
			verify(embeddableImageService).createAndEmbedImage(any(EmbeddableImageContext.class), any(UploadedFile.class));
		}

		@Test
		void should_createEmbeddableImageWork_when_UserAutorAndRaetselOwner() {

			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			raetsel.owner = authenticatedUser.getUuid();

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(raetselDao.findById(RAETSEL_ID)).thenReturn(raetsel);

			doNothing().when(uploadScanner)
				.scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSELID_NEU).withTextart(Textart.FRAGE);
			CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
			requestDto.setContext(context);
			requestDto.setFile(uploadedFile);

			EmbeddableImageResponseDto responseDto = new EmbeddableImageResponseDto().with(context);

			when(embeddableImageService.createAndEmbedImage(any(EmbeddableImageContext.class), any(UploadedFile.class)))
				.thenReturn(responseDto);

			// Act
			EmbeddableImageResponseDto result = service.createEmbeddableImage(requestDto);

			// Assert
			assertNotNull(result);

			verify(authCtx).getUser();
			verify(authCtx, never()).isUserInRole("ADMIN");
			verify(authCtx, never()).isUserInRole("AUTOR");

			verify(raetselDao).findById(anyString());

			verify(uploadScanner).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
			verify(embeddableImageService).createAndEmbedImage(any(EmbeddableImageContext.class), any(UploadedFile.class));
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
			when(raetselDao.findById(RAETSEL_ID)).thenReturn(raetsel);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.LOESUNG);

			ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context).withRelativerPfad(RELATIVER_PFAD).withUpload(uploadedFile);

			// Act
			try {

				service.replaceTheEmbeddableImage(requestDto);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(uploadScanner, never()).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class));

			}

		}

	@Test
		void should_replaceTheEmbeddableImageThrowWebApplicationException403_when_userNeitherAutorNorAdmin() throws Exception {

			// Arrange
			when(authCtx.getUser()).thenReturn(authenticatedUser);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.LOESUNG);
			ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context).withRelativerPfad(RELATIVER_PFAD).withUpload(uploadedFile);

			// Act
			try {

				service.replaceTheEmbeddableImage(requestDto);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(raetselDao, never()).findById(anyString());
				verify(uploadScanner, never()).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class));

			}

		}

		@Test
		void should_replaceTheEmbeddableImageThrowWebApplicationException404_when_raetselNotFound() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });

			when(authCtx.getUser()).thenReturn(authenticatedUser);

			when(raetselDao.findById(RAETSEL_ID)).thenReturn(null);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.LOESUNG);

			ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
				.withRelativerPfad(RELATIVER_PFAD).withUpload(uploadedFile);

			// Act
			try {

				service.replaceTheEmbeddableImage(requestDto);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(404, e.getResponse().getStatus());

				verify(authCtx).getUser();
				verify(authCtx, never()).isUserInRole("ADMIN");
				verify(authCtx, never()).isUserInRole("AUTOR");

				verify(uploadScanner, never()).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class));

			}

		}

		@Test
		void should_replaceTheEmbeddableImageReturnError_when_UploadFormatException() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "ADMIN" });

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.TRUE);
			when(raetselDao.findById(RAETSEL_ID)).thenReturn(raetsel);

			doThrow(new UploadFormatException("Hilfe Virus one, one, one, ...")).when(uploadScanner)
				.scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.LOESUNG);

			ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
				.withRelativerPfad(RELATIVER_PFAD).withUpload(uploadedFile);

			// Act
			try {

				service.replaceTheEmbeddableImage(requestDto);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				verify(authCtx).getUser();
				verify(authCtx).isUserInRole("ADMIN");
				verify(authCtx).isUserInRole("AUTOR");
				verify(uploadScanner).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
				verify(embeddableImageService, never()).replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class));

				assertEquals(400, e.getResponse().getStatus());
			}
		}

		@Test
		void should_replaceTheEmbeddableImageReturnSuccess_when_UserAutorAndOwner() throws Exception {

			// Arrange
			authenticatedUser = authenticatedUser.withRoles(new String[] { "AUTOR" });
			raetsel.owner = authenticatedUser.getUuid();

			when(authCtx.getUser()).thenReturn(authenticatedUser);
			when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
			when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);
			when(raetselDao.findById(RAETSEL_ID)).thenReturn(raetsel);

			doNothing().when(uploadScanner)
				.scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());

			EmbeddableImageResponseDto expectedResult = new EmbeddableImageResponseDto();

			when(embeddableImageService.replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class)))
				.thenReturn(expectedResult);

			EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(RAETSEL_ID).withTextart(Textart.LOESUNG);

			ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
				.withRelativerPfad(RELATIVER_PFAD).withUpload(uploadedFile);

			// Act
			service.replaceTheEmbeddableImage(requestDto);

			verify(authCtx).getUser();
			verify(authCtx).isUserInRole("ADMIN");
			verify(authCtx).isUserInRole("AUTOR");
			verify(raetselDao).findById(RAETSEL_ID);
			verify(uploadScanner).scanUpload(any(ReplaceEmbeddableImageRequestDto.class), anyList());
			verify(embeddableImageService).replaceEmbeddedImage(any(ReplaceEmbeddableImageRequestDto.class));
		}
	}
}
