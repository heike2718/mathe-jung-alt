// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.embeddable_images.dto.Textart;
import de.egladil.mja_api.domain.raetsel.dto.EmbeddableImageInfo;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * EmbeddedImagesServiceTest
 */
@QuarkusTest
// @TestProfile(FullDatabaseTestProfile.class)
public class EmbeddedImagesServiceTest {

	private static final String ADMIN_UUID = "86348fae-b322-47bb-ba51-a0f5fed9fcdd";

	private static final String AUTOR_UUID = "1cccb349-5085-478b-ba52-b49ba913e48c";

	private static final String RAETSEL_ID = "554d4994-90b1-4baf-a7a0-cb5cb3b54ac6";

	AuthenticatedUser admin;

	AuthenticatedUser autor;

	@InjectMock
	AuthenticationContext authCtx;

	@InjectMock
	RaetselDao raetselDao;

	@InjectMock
	RaetselService raetselService;

	@Inject
	EmbeddedImagesService service;

	@BeforeEach
	void setUp() {

		admin = new AuthenticatedUser(ADMIN_UUID).withBenutzerart(Benutzerart.ADMIN).withRoles(new String[] { "ADMIN" });
		autor = new AuthenticatedUser(AUTOR_UUID).withBenutzerart(Benutzerart.AUTOR)
			.withRoles(new String[] { "AUTOR" });

	}

	@Test
	void should_getEmbeddedImagesReturnListOf2_when_userIsAdminButNotOwner() {

		// Arrange
		PersistentesRaetsel persistentesRaetsel = new PersistentesRaetsel();
		persistentesRaetsel.uuid = RAETSEL_ID;
		persistentesRaetsel.owner = AUTOR_UUID;

		List<EmbeddableImageInfo> imageInfosFrage = new ArrayList<>();

		imageInfosFrage.add(new EmbeddableImageInfo(
			"/resources/f/faf721a9-01019.eps", true, Textart.FRAGE));
		imageInfosFrage.add(new EmbeddableImageInfo(
			"/resources/8/8548cc1c-00001.eps", false, Textart.FRAGE));

		List<EmbeddableImageInfo> imageInfosLoesung = new ArrayList<>();

		imageInfosLoesung.add(new EmbeddableImageInfo(
			"/resources/7/74bf1426-00001.eps", false, Textart.LOESUNG));

		imageInfosLoesung.add(new EmbeddableImageInfo(
			"/resources/5/540deb41-00516.eps", true, Textart.LOESUNG));

		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(raetselService.loadEmbeddableImageInfos(any(PersistentesRaetsel.class)))
			.thenReturn(Pair.of(imageInfosFrage, imageInfosLoesung));
		when(authCtx.getUser()).thenReturn(admin);

		// Act
		List<GeneratedFile> result = service.getEmbeddedImages(RAETSEL_ID);

		// Assert
		assertEquals(2, result.size());

		{

			Optional<GeneratedFile> optFile = result.stream().filter(f -> "faf721a9-01019.eps".equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		{

			Optional<GeneratedFile> optFile = result.stream().filter(f -> "540deb41-00516.eps".equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		verify(raetselDao).findById(RAETSEL_ID);
		verify(raetselService).loadEmbeddableImageInfos(any(PersistentesRaetsel.class));
	}

	@Test
	void should_getEmbeddedImagesReturnListOf2_when_userIsNotAdminButOwner() {

		// Arrange
		PersistentesRaetsel persistentesRaetsel = new PersistentesRaetsel();
		persistentesRaetsel.uuid = RAETSEL_ID;
		persistentesRaetsel.owner = AUTOR_UUID;

		List<EmbeddableImageInfo> imageInfosFrage = new ArrayList<>();

		imageInfosFrage.add(new EmbeddableImageInfo(
			"/resources/f/faf721a9-01019.eps", true, Textart.FRAGE));
		imageInfosFrage.add(new EmbeddableImageInfo(
			"/resources/8/8548cc1c-00001.eps", false, Textart.FRAGE));

		List<EmbeddableImageInfo> imageInfosLoesung = new ArrayList<>();

		imageInfosLoesung.add(new EmbeddableImageInfo(
			"/resources/7/74bf1426-00001.eps", false, Textart.LOESUNG));

		imageInfosLoesung.add(new EmbeddableImageInfo(
			"/resources/5/540deb41-00516.eps", true, Textart.LOESUNG));

		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(raetselService.loadEmbeddableImageInfos(any(PersistentesRaetsel.class)))
			.thenReturn(Pair.of(imageInfosFrage, imageInfosLoesung));
		when(authCtx.getUser()).thenReturn(autor);

		// Act
		List<GeneratedFile> result = service.getEmbeddedImages(RAETSEL_ID);

		// Assert
		assertEquals(2, result.size());

		{

			Optional<GeneratedFile> optFile = result.stream().filter(f -> "faf721a9-01019.eps".equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		{

			Optional<GeneratedFile> optFile = result.stream().filter(f -> "540deb41-00516.eps".equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		verify(raetselDao).findById(RAETSEL_ID);
		verify(raetselService).loadEmbeddableImageInfos(any(PersistentesRaetsel.class));
	}

	@Test
	void should_getEmbeddedImagesThrow403_when_userIsNotAdminAndNotOwner() {

		// Arrange
		PersistentesRaetsel persistentesRaetsel = new PersistentesRaetsel();
		persistentesRaetsel.uuid = RAETSEL_ID;
		persistentesRaetsel.owner = ADMIN_UUID;

		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(authCtx.getUser()).thenReturn(autor);

		// Act
		try {

			service.getEmbeddedImages(RAETSEL_ID);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			Response response = e.getResponse();
			assertNotNull(response);
			assertEquals(403, response.getStatus());

			verify(raetselDao).findById(RAETSEL_ID);
			verify(raetselService, never()).loadEmbeddableImageInfos(any(PersistentesRaetsel.class));
		}
	}

	@Test
	void should_getEmbeddedImagesThrow404_when_raetselDoesNotExist() {

		// Arrange
		when(raetselDao.findById(RAETSEL_ID)).thenReturn(null);

		// Act
		try {

			service.getEmbeddedImages(RAETSEL_ID);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			Response response = e.getResponse();
			assertNotNull(response);
			assertEquals(404, response.getStatus());

			verify(raetselDao).findById(RAETSEL_ID);
			verify(authCtx, never()).getUser();
			verify(raetselService, never()).loadEmbeddableImageInfos(any(PersistentesRaetsel.class));
		}
	}
}
