// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * DeskriptorenServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
public class DeskriptorenServiceImplTest {

	@Mock
	DeskriptorenRepository repository;

	@InjectMocks
	DeskriptorenServiceImpl service;

	@Nested
	class MappingTests {

		@Test
		void should_mapToDeskriptorenReturnDeskriptoren() {

			// Arrange
			String deskriptorenIds = "1,4,5";
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
		void should_filterReturnAll_when_KontextNoop() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.NOOP;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> result = service.filterByKontext(kontext, alleDeskriptoren);

			// Assert
			assertEquals(8, result.size());
			verify(repository, never()).listAll();
		}

		@Test
		void should_filterReturnAll_when_KontextBilder() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.BILDER;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> result = service.filterByKontext(kontext, alleDeskriptoren);

			// Assert
			assertEquals(8, result.size());
			verify(repository, never()).listAll();
		}

		@Test
		void should_filterReturnNurMitQuelle_when_KontextQuellen() {

			// Arrange
			DeskriptorSuchkontext kontext = DeskriptorSuchkontext.QUELLEN;
			List<Deskriptor> alleDeskriptoren = createAlleDeskriptoren();

			// Act
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren);

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
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren);

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
			List<Deskriptor> deskriptoren = service.filterByKontext(kontext, alleDeskriptoren);

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
