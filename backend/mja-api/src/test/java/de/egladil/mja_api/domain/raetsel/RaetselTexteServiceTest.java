// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * RaetselTexteServiceTest
 */
@QuarkusTest
public class RaetselTexteServiceTest {

	private static final String ADMIN_UUID = "86348fae-b322-47bb-ba51-a0f5fed9fcdd";

	private static final String AUTOR_UUID = "1cccb349-5085-478b-ba52-b49ba913e48c";

	private static final String SCHLUESSEL = "56789";

	private static final String RAETSEL_ID = "554d4994-90b1-4baf-a7a0-cb5cb3b54ac6";

	AuthenticatedUser admin;

	AuthenticatedUser autor;

	PersistentesRaetsel persistentesRaetsel;

	@InjectMock
	AuthenticationContext authCtx;

	@InjectMock
	RaetselDao raetselDao;

	@Inject
	RaetselTexteService service;

	@BeforeEach
	void setUp() {

		admin = new AuthenticatedUser(ADMIN_UUID).withBenutzerart(Benutzerart.ADMIN).withRoles(new String[] { "ADMIN" });
		autor = new AuthenticatedUser(AUTOR_UUID).withBenutzerart(Benutzerart.AUTOR)
			.withRoles(new String[] { "AUTOR" });

		persistentesRaetsel = new PersistentesRaetsel();
		persistentesRaetsel.uuid = RAETSEL_ID;
		persistentesRaetsel.owner = AUTOR_UUID;
		persistentesRaetsel.schluessel = SCHLUESSEL;
		persistentesRaetsel.frage = "2 + 0 + 2 + 2 = ?";
		persistentesRaetsel.loesung = "6";

	}

	@Test
	void should_getTexteReturnListOf2_when_userIsAdminButNotOwner() {

		// Arrange
		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(authCtx.getUser()).thenReturn(admin);

		// Act
		List<GeneratedFile> result = service.getTexte(RAETSEL_ID);

		// Assert
		assertEquals(2, result.size());

		{

			String filename = SCHLUESSEL + ".tex";

			Optional<GeneratedFile> optFile = result.stream().filter(f -> filename.equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		{

			String filenameFrage = SCHLUESSEL + "_l.tex";

			Optional<GeneratedFile> optFile = result.stream().filter(f -> filenameFrage.equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		verify(raetselDao).findById(RAETSEL_ID);
	}

	@Test
	void should_getTexteReturnListOf1_when_thereIsNoLoesungstext() {

		// Arrange
		persistentesRaetsel.loesung = null;

		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(authCtx.getUser()).thenReturn(admin);

		// Act
		List<GeneratedFile> result = service.getTexte(RAETSEL_ID);

		// Assert
		assertEquals(1, result.size());

		GeneratedFile generatedFile = result.get(0);
		assertEquals(SCHLUESSEL + ".tex", generatedFile.getFileName());
		assertTrue(generatedFile.getFileData().length > 0);

		verify(raetselDao).findById(RAETSEL_ID);
	}

	@Test
	void should_getTexteReturnListOf2_when_userIsNotAdminButOwner() {

		// Arrange
		persistentesRaetsel.owner = AUTOR_UUID;
		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(authCtx.getUser()).thenReturn(autor);

		// Act
		List<GeneratedFile> result = service.getTexte(RAETSEL_ID);

		// Assert
		assertEquals(2, result.size());

		{

			String filename = SCHLUESSEL + ".tex";

			Optional<GeneratedFile> optFile = result.stream().filter(f -> filename.equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		{

			String filenameFrage = SCHLUESSEL + "_l.tex";

			Optional<GeneratedFile> optFile = result.stream().filter(f -> filenameFrage.equals(f.getFileName())).findFirst();
			assertTrue(optFile.isPresent());

			GeneratedFile generatedFile = optFile.get();
			assertNotNull(generatedFile.getFileData());
			assertTrue(generatedFile.getFileData().length > 0);
		}

		verify(raetselDao).findById(RAETSEL_ID);
	}

	@Test
	void should_getTexteThrow403_when_userIsNotAdminAndNotOwner() {

		// Arrange
		persistentesRaetsel.owner = ADMIN_UUID;
		when(raetselDao.findById(RAETSEL_ID)).thenReturn(persistentesRaetsel);
		when(authCtx.getUser()).thenReturn(autor);

		// Act
		try {

			service.getTexte(RAETSEL_ID);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			// Assert
			assertEquals(403, e.getResponse().getStatus());
			verify(raetselDao).findById(RAETSEL_ID);
		}
	}

	@Test
	void should_getTexteThrow404_when_raetselDoesNotExist() {

		// Arrange
		when(raetselDao.findById(RAETSEL_ID)).thenReturn(null);
		when(authCtx.getUser()).thenReturn(autor);

		// Act
		try {

			service.getTexte(RAETSEL_ID);
			fail("keine WebApplicationException");
		} catch (WebApplicationException e) {

			// Assert
			assertEquals(404, e.getResponse().getStatus());
			verify(raetselDao).findById(RAETSEL_ID);
		}
	}
}
