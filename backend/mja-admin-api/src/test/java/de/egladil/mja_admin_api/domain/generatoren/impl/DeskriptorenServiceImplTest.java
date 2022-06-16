// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import de.egladil.mja_admin_api.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mja_admin_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_admin_api.domain.deskriptoren.impl.DeskriptorenServiceImpl;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

/**
 * DeskriptorenServiceImplTest
 */
@QuarkusTest
public class DeskriptorenServiceImplTest {

	@InjectMock
	DeskriptorenRepository repository;

	@Inject
	DeskriptorenServiceImpl service;

	@Nested
	class MappingTests {

		@Test
		void should_mapToDeskriptorenReturnDeskriptoren() {

			// Arrange
			String deskriptorenIds = ",1,,4,,5,";
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			when(repository.listAll()).thenReturn(alleDeskriptoren);

			// Act
			List<Deskriptor> deskriptoren = service.mapToDeskriptoren(deskriptorenIds);

			// Assert
			assertEquals(3, deskriptoren.size());
			verify(repository).listAll();

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(1).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Mathe", optDesk.get().name);
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(4).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Minikänguru", optDesk.get().name);
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(5).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Zeitschrift", optDesk.get().name);
			}
		}

		@Test
		void should_mapToDeskriptorenReturnEmptyList_when_parameterNull() {

			// Act
			List<Deskriptor> deskriptoren = service.mapToDeskriptoren(null);

			// Assert
			assertEquals(0, deskriptoren.size());
			verify(repository, never()).listAll();

		}

	}

	@Nested
	class FilterTests {

		@Test
		void should_filterReturnNone_when_KontextNoopOrdinary() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.NOOP;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> result = service.filterByKontext(kontext, alleDeskriptoren, false);

			// Assert
			assertEquals(0, result.size());
			verify(repository, never()).listAll();
		}

		@Test
		void should_filterReturnNone_when_KontextNoopAdmin() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.NOOP;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> result = service.filterByKontext(kontext, alleDeskriptoren, true);

			// Assert
			assertEquals(8, result.size());
			verify(repository, never()).listAll();
		}

		@Test
		void should_filterReturnNone_when_KontextBilder() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.BILDER;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> result = service.filterByKontext(kontext, alleDeskriptoren, true);

			// Assert
			assertEquals(0, result.size());
			verify(repository, never()).listAll();
		}

		@Test
		void should_filterReturnNurMitQuelle_when_KontextQuellen() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.QUELLEN;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren, true);

			// Assert
			assertEquals(3, deskriptoren.size());
			verify(repository, never()).listAll();

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(5).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Zeitschrift", optDesk.get().name);
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(7).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Person", optDesk.get().name);
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(8).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Buch", optDesk.get().name);
			}
		}

		@Test
		void should_filterReturnNurMitMedium_when_KontextMedien() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.MEDIEN;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren, true);

			// Assert
			assertEquals(2, deskriptoren.size());
			verify(repository, never()).listAll();

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(5).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Zeitschrift", optDesk.get().name);
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(8).equals(d.id)).findFirst();
				assertTrue(optDesk.isPresent());
				assertEquals("Buch", optDesk.get().name);
			}
		}

		@Test
		void should_filterReturnOhneQuelleUndMedium_when_KontextRaetsel() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.RAETSEL;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren, true);

			// Assert
			assertEquals(5, deskriptoren.size());
			verify(repository, never()).listAll();

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(5).equals(d.id)).findFirst();
				assertFalse(optDesk.isPresent());

			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(7).equals(d.id)).findFirst();
				assertFalse(optDesk.isPresent());
			}

			{

				Optional<Deskriptor> optDesk = deskriptoren.stream().filter(d -> Long.valueOf(8).equals(d.id)).findFirst();
				assertFalse(optDesk.isPresent());
			}
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

			Deskriptor deskriptor = new Deskriptor("Minikänguru", false, "RAETSEL");
			deskriptor.id = 4l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Zeitschrift", false, "MEDIEN,QUELLEN");
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
