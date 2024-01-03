// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * DeskriptorenServiceTest
 */
@QuarkusTest
// @TestProfile(FullDatabaseAdminTestProfile.class)
public class DeskriptorenServiceTest {

	@InjectMock
	AuthenticationContext authCtx;

	@InjectMock
	DeskriptorenRepository repository;

	@Inject
	DeskriptorenService service;

	@Nested
	class MappingTests {

		@Test
		void should_mapToDeskriptorenReturnEmptyList_when_parameterNull() {

			// Act
			List<Deskriptor> deskriptoren = service.mapToDeskriptoren(null);

			// Assert
			assertEquals(0, deskriptoren.size());
			verify(repository, never()).listAll();

		}

		@Test
		void should_transformToDeskriptorenOrdinalWork() {

			// Arrange
			when(repository.listAll()).thenReturn(createAlleDeskriptoren());

			// Act
			String result = service.transformToDeskriptorenOrdinal("Minikänguru,Klasse 1,Mathe,unbekannt,Minikänguru");

			// Assert
			assertEquals("1,3,4", result);

		}

	}

	@Nested
	class SerializationTests {

		@Test
		void should_sortAndStringifyIdsDeskriptorenReturnNull_when_parameterNull() {

			// Act
			String result = service.sortAndStringifyIdsDeskriptoren(null);

			// Assert
			assertNull(result);

		}

		@Test
		void should_serializeReturnNull_when_parameterEmptyArray() {

			// Act
			String result = service.sortAndStringifyIdsDeskriptoren(new ArrayList<>());

			// Assert
			assertNull(result);

		}

		@Test
		void should_sortAndStringifyIdsDeskriptorenOrderById() {

			// Arrange
			String expected = ",2,,5,,7,";

			List<Deskriptor> deskriptoren = new ArrayList<>();

			{

				Deskriptor d = new Deskriptor();
				d.id = 5L;
				d.name = "Bla";
				deskriptoren.add(d);
			}

			{

				Deskriptor d = new Deskriptor();
				d.id = 7L;
				d.name = "Foo";
				deskriptoren.add(d);
			}

			{

				Deskriptor d = new Deskriptor();
				d.id = 2L;
				d.name = "Blubb";
				deskriptoren.add(d);
			}

			// Act
			String result = service.sortAndStringifyIdsDeskriptoren(deskriptoren);

			// Assert
			assertEquals(expected, result);

		}

	}

	@Nested
	class LoadTests {

		@Test
		void should_loadDeskriptorenRaetselReturnAllWithAdmin_when_BenutzerartAdmin() {

			AuthenticatedUser user = new AuthenticatedUser("abcdef-012345")
				.withBenutzerart(Benutzerart.ADMIN);
			when(authCtx.getUser()).thenReturn(user);

			// Arrange
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<DeskriptorUI> result = service.loadDeskriptoren();

			// Assert
			assertEquals(8, result.size());
			verify(repository).listAll();

		}

		@Test
		void should_loadDeskriptorenRaetselReturnAllWithAdmin_when_BenutzerartAUTOR() {

			AuthenticatedUser user = new AuthenticatedUser("abcdef-012345")
				.withBenutzerart(Benutzerart.AUTOR);
			when(authCtx.getUser()).thenReturn(user);

			// Arrange
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<DeskriptorUI> result = service.loadDeskriptoren();

			// Assert
			assertEquals(8, result.size());
			verify(repository).listAll();

		}

		@Test
		void should_loadDeskriptorenRaetselReturnPublic_when_BenutzerartSTANDARD() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("abcdef-012345")
				.withBenutzerart(Benutzerart.STANDARD);
			when(authCtx.getUser()).thenReturn(user);

			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<DeskriptorUI> result = service.loadDeskriptoren();

			// Assert
			assertEquals(4, result.size());
			verify(repository).listAll();

		}

		@Test
		void should_loadDeskriptorenRaetselThrowWebApplicationException_when_BenutzerartANONYM() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("abcdef-012345")
				.withBenutzerart(Benutzerart.ANONYM);
			when(authCtx.getUser()).thenReturn(user);

			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			try {

				service.loadDeskriptoren();
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());
			}

		}

		@Test
		void should_findByNameReturnTheDeskriptor_when_exists() {

			// Arrange
			when(repository.listAll()).thenReturn(createAlleDeskriptoren());
			String name = "Minikänguru";

			// Act
			Optional<Deskriptor> optTreffer = service.findByName(name);

			// Assert
			assertTrue(optTreffer.isPresent());

			Deskriptor result = optTreffer.get();

			assertEquals(name, result.name);
			assertEquals(4L, result.id);

		}

		@Test
		void should_findByNameReturnEmpty_when_notExists() {

			// Arrange
			when(repository.listAll()).thenReturn(createAlleDeskriptoren());
			String name = "Grundschule";

			// Act
			Optional<Deskriptor> optTreffer = service.findByName(name);

			// Assert
			assertTrue(optTreffer.isEmpty());
		}

	}

	/**
	 * @return
	 */
	List<Deskriptor> createAlleDeskriptoren() {

		List<Deskriptor> alleDeskriptoren = new ArrayList<>();

		{

			Deskriptor deskriptor = new Deskriptor("Mathe", false);
			deskriptor.id = 1l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Arithmetik", false);
			deskriptor.id = 2l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Klasse 1", false);
			deskriptor.id = 3l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Minikänguru", true);
			deskriptor.id = 4l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Zeitschrift", true);
			deskriptor.id = 5l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Nachbau", true);
			deskriptor.id = 6l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Person", true);
			deskriptor.id = 7l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Buch", false);
			deskriptor.id = 8l;
			alleDeskriptoren.add(deskriptor);
		}
		return alleDeskriptoren;
	}

}
