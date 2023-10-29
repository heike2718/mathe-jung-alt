// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.exceptions.UploadFormatException;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
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

	@Test
	void should_saveTheUploadThrowWebApplicationException403_when_userAutorButNotOwner() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("gdgagsa");

		PersistentesRaetsel raetsel = new PersistentesRaetsel();
		raetsel.owner = "adhaiohq";
		raetsel.uuid = raetselId;

		when(authCtx.getUser()).thenReturn(authenticatedUser);
		when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
		when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);
		when(raetselDao.getWithID(raetselId)).thenReturn(raetsel);

		// Act
		try {

			service.saveTheUpload(raetselId, uploadData, relativerPfad);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			assertEquals(403, e.getResponse().getStatus());

			verify(processUploadService, never()).processFile(any(UploadData.class));
			verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
			verify(grafikService, never()).grafikSpeichern(any(UploadRequestDto.class));

		}

	}

	@Test
	void should_saveTheUploadThrowWebApplicationException403_when_userNeitherAutorNorAdmin() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("gdgagsa").withRoles(new String[] {});

		PersistentesRaetsel raetsel = new PersistentesRaetsel();
		raetsel.owner = "adhaiohq";
		raetsel.uuid = raetselId;

		when(authCtx.getUser()).thenReturn(authenticatedUser);
		when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
		when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.FALSE);
		when(raetselDao.getWithID(raetselId)).thenReturn(raetsel);

		// Act
		try {

			service.saveTheUpload(raetselId, uploadData, relativerPfad);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			assertEquals(403, e.getResponse().getStatus());

			verify(processUploadService, never()).processFile(any(UploadData.class));
			verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
			verify(grafikService, never()).grafikSpeichern(any(UploadRequestDto.class));

		}

	}

	@Test
	void should_saveTheUploadThrowWebApplicationException404_when_raetselNotFound() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		when(authCtx.getUser()).thenReturn(new AuthenticatedUser("gdgagsa"));
		when(raetselDao.getWithID(raetselId)).thenReturn(null);

		// Act
		try {

			service.saveTheUpload(raetselId, uploadData, relativerPfad);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			assertEquals(404, e.getResponse().getStatus());

			verify(processUploadService, never()).processFile(any(UploadData.class));
			verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
			verify(grafikService, never()).grafikSpeichern(any(UploadRequestDto.class));

		}

	}

	@Test
	void should_saveTheUploadReturnError_when_IOException() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("gdgagsa").withRoles(new String[] { "ADMIN" });

		PersistentesRaetsel raetsel = new PersistentesRaetsel();
		raetsel.owner = "adhaiohq";
		raetsel.uuid = raetselId;

		when(authCtx.getUser()).thenReturn(authenticatedUser);
		when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.TRUE);
		when(raetselDao.getWithID(raetselId)).thenReturn(raetsel);
		doThrow(new MjaRuntimeException("gibet nicht")).when(processUploadService).processFile(any(UploadData.class));

		// Act
		MessagePayload result = service.saveTheUpload(raetselId, uploadData, relativerPfad);
		assertEquals("Es ist ein Fehler aufgetreten. Bitte senden Sie eine Mail an info@egladil.de.", result.getMessage());
		assertEquals("ERROR", result.getLevel());

		verify(uploadScanner, never()).scanUpload(any(UploadRequestDto.class));
		verify(grafikService, never()).grafikSpeichern(any(UploadRequestDto.class));

	}

	@Test
	void should_saveTheUploadReturnError_when_UploadFormatException() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("gdgagsa").withRoles(new String[] { "ADMIN" });

		PersistentesRaetsel raetsel = new PersistentesRaetsel();
		raetsel.owner = "adhaiohq";
		raetsel.uuid = raetselId;

		when(authCtx.getUser()).thenReturn(authenticatedUser);
		when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.TRUE);
		when(raetselDao.getWithID(raetselId)).thenReturn(raetsel);
		doNothing().when(processUploadService).processFile(any(UploadData.class));

		doThrow(new UploadFormatException("Hilfe Virus one, one, one, ...")).when(uploadScanner)
			.scanUpload(any(UploadRequestDto.class));

		// Act
		MessagePayload result = service.saveTheUpload(raetselId, uploadData, relativerPfad);
		assertEquals("Die Datei ist nicht akzeptabel.", result.getMessage());
		assertEquals("ERROR", result.getLevel());

		verify(grafikService, never()).grafikSpeichern(any(UploadRequestDto.class));
	}

	@Test
	void should_saveTheUploadReturnSuccess_when_UserAutorAndOwner() throws Exception {

		// Arrange
		String raetselId = "hksldha";
		File file = new File("/media/veracrypt1/knobelarchiv_2/latex/resources/001/00000.eps");
		UploadData uploadData = new UploadData("00000.eps", file);
		String relativerPfad = "/resources/001/00000.eps";

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("gdgagsa").withRoles(new String[] { "ADMIN" });

		PersistentesRaetsel raetsel = new PersistentesRaetsel();
		raetsel.owner = "gdgagsa";
		raetsel.uuid = raetselId;

		when(authCtx.getUser()).thenReturn(authenticatedUser);
		when(authCtx.isUserInRole("ADMIN")).thenReturn(Boolean.FALSE);
		when(authCtx.isUserInRole("AUTOR")).thenReturn(Boolean.TRUE);
		when(raetselDao.getWithID(raetselId)).thenReturn(raetsel);
		doNothing().when(processUploadService).processFile(any(UploadData.class));

		doNothing().when(uploadScanner)
			.scanUpload(any(UploadRequestDto.class));

		MessagePayload mp = MessagePayload.ok();
		when(grafikService.grafikSpeichern(any(UploadRequestDto.class))).thenReturn(mp);

		// Act
		MessagePayload result = service.saveTheUpload(raetselId, uploadData, relativerPfad);
		assertEquals("ok", result.getMessage());
		assertEquals("INFO", result.getLevel());

		verify(authCtx, times(2)).getUser();
		verify(authCtx).isUserInRole("ADMIN");
		verify(authCtx).isUserInRole("AUTOR");
		verify(raetselDao).getWithID(raetselId);
		verify(processUploadService).processFile(any(UploadData.class));
		verify(uploadScanner).scanUpload(any(UploadRequestDto.class));
		verify(grafikService).grafikSpeichern(any(UploadRequestDto.class));
	}
}
