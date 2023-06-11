// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorUI;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;

/**
 * DeskriptorenServiceImplTest
 */
@QuarkusTest
@TestProfile(FullDatabaseTestProfile.class)
public class DeskriptorenServiceImplTest {

	@InjectMock
	DeskriptorenRepository repository;

	@Inject
	DeskriptorenServiceImpl service;

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
		@TestSecurity(authorizationEnabled = false)
		void should_loadDeskriptorenRaetselReturnAllWithAdmin_when_adminTrue() {

			// Arrange
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<DeskriptorUI> result = service.loadDeskriptorenRaetsel(true);

			// Assert
			assertEquals(5, result.size());
			verify(repository).listAll();

		}

		@Test
		void should_loadDeskriptorenRaetselReturnPublic_when_adminFalse() {

			// Arrange
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();
			assertEquals(8, alleDeskriptoren.size());
			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<DeskriptorUI> result = service.loadDeskriptorenRaetsel(false);

			// Assert
			assertEquals(3, result.size());
			verify(repository).listAll();

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

			Deskriptor deskriptor = new Deskriptor("Mathe", false, "RAETSEL");
			deskriptor.id = 1l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Arithmetik", false, "RAETSEL");
			deskriptor.id = 2l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Klasse 1", false, "RAETSEL");
			deskriptor.id = 3l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Minikänguru", true, "RAETSEL");
			deskriptor.id = 4l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Zeitschrift", true, "MEDIEN,QUELLEN");
			deskriptor.id = 5l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Nachbau", true, "RAETSEL");
			deskriptor.id = 6l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Person", true, "QUELLEN");
			deskriptor.id = 7l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Buch", false, "MEDIEN,QUELLEN");
			deskriptor.id = 8l;
			alleDeskriptoren.add(deskriptor);
		}
		return alleDeskriptoren;
	}

}
