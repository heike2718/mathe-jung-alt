// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

	@Test
	void should_mapToDeskriptorenReturnDeskriptoren() {

		// Arrange
		String deskriptorenIds = "1,4,5";
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

			Deskriptor deskriptor = new Deskriptor("Minikänguru", false);
			deskriptor.id = 4l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Zeitschrift", false);
			deskriptor.id = 5l;
			alleDeskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Nachbau", true);
			deskriptor.id = 6l;
			alleDeskriptoren.add(deskriptor);
		}

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
