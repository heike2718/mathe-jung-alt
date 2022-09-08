// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.quellen.QuelleReadonly;
import de.egladil.mja_api.domain.quellen.QuellenRepository;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.impl.QuellenServiceImpl;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectMock;

/**
 * QuellenServiceImplTest
 */
@QuarkusTest
@TestProfile(FullDatabaseTestProfile.class)
public class QuellenServiceImplTest {

	@InjectMock
	QuellenRepository quellenRepository;

	@InjectMock
	DeskriptorenService deskriptorenService;

	@Inject
	QuellenServiceImpl service;

	@Nested
	class SucheOhneDeskriptorenTests {

		@Test
		void should_sucheQuellenReturnEmptyList_when_parameterBlank() {

			// Arrange
			String suchstring = "  ";

			// Act
			try {

				service.sucheQuellen(new Suchfilter(suchstring, ""));
				fail("keine IllegalArgumentException");
			} catch (IllegalArgumentException e) {

				assertEquals("suchstring oder deskriptorenIds erforderlich", e.getMessage());
			}

			// Assert
			verify(quellenRepository, never()).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService, never()).mapToDeskriptoren(any());
		}

		@Test
		void should_sucheQuellenReturnQuelle_when_einTrefferMitPerson() {

			// Arrange
			PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
			persistenteQuelle.setDeskriptoren("1,3");
			persistenteQuelle.setPerson("Heike Winkelvoß");
			persistenteQuelle.setQuellenart(Quellenart.PERSON);
			persistenteQuelle.setSortNumber(1);
			persistenteQuelle.setUuid("q-uuid-1");

			String suchstring = "Winkel";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(Collections.singletonList(persistenteQuelle));

			List<Deskriptor> deskriptoren = getDeskriptorenForTest();

			when(deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren())).thenReturn(deskriptoren);

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, ""));

			// Assert
			assertEquals(1, result.size());
			QuelleReadonly quelle = result.get(0);
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService).mapToDeskriptoren(persistenteQuelle.getDeskriptoren());

			assertEquals(2, quelle.getDeskriptoren().size());
			assertEquals("q-uuid-1", quelle.getId());
			assertNull(quelle.getMediumUuid());
			assertEquals("Heike Winkelvoß", quelle.getName());
			assertEquals(Quellenart.PERSON, quelle.getQuellenart());

		}

		@Test
		void should_sucheQuellenReturnQuelle_when_treffermengeLeer() {

			// Arrange
			String suchstring = "Winkel";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(new ArrayList<>());

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, ""));

			// Assert
			assertEquals(0, result.size());
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService, never()).mapToDeskriptoren(any());
		}

		@Test
		void should_sucheQuellenReturnQuelle_when_treffermengeNull() {

			// Arrange
			String suchstring = "Winkel";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(null);

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, ""));

			// Assert
			assertEquals(0, result.size());
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService, never()).mapToDeskriptoren(any());
		}

		@Test
		void should_sucheQuellenReturnQuelle_when_einTrefferMitZeitschrift() {

			// Arrange
			PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
			persistenteQuelle.setDeskriptoren("1,3");
			persistenteQuelle.setMediumTitel("alpha");
			persistenteQuelle.setQuellenart(Quellenart.ZEITSCHRIFT);
			persistenteQuelle.setJahrgang("1978");
			persistenteQuelle.setMediumUuid("m-uuid-1");
			persistenteQuelle.setAusgabe("3");
			persistenteQuelle.setSeite("13");
			persistenteQuelle.setSortNumber(1);
			persistenteQuelle.setUuid("q-uuid-2");

			String suchstring = "alpha";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(Collections.singletonList(persistenteQuelle));

			List<Deskriptor> deskriptoren = getDeskriptorenForTest();

			when(deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren())).thenReturn(deskriptoren);

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, null));

			// Assert
			assertEquals(1, result.size());
			QuelleReadonly quelle = result.get(0);
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService).mapToDeskriptoren(persistenteQuelle.getDeskriptoren());

			assertEquals(2, quelle.getDeskriptoren().size());
			assertEquals("q-uuid-2", quelle.getId());
			assertEquals("m-uuid-1", quelle.getMediumUuid());
			assertEquals("alpha (3) 1978, S.13", quelle.getName());
			assertEquals(Quellenart.ZEITSCHRIFT, quelle.getQuellenart());

		}

		@Test
		void should_sucheQuellenReturnQuelle_when_einTrefferMitBuch() {

			// Arrange
			PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
			persistenteQuelle.setDeskriptoren("1,3");
			persistenteQuelle.setMediumTitel("2x3 und Spaß dabei");
			persistenteQuelle.setMediumUuid("m-uuid-2");
			persistenteQuelle.setQuellenart(Quellenart.BUCH);
			persistenteQuelle.setSeite("42");
			persistenteQuelle.setSortNumber(3);
			persistenteQuelle.setUuid("q-uuid-3");

			String suchstring = "2x3";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(Collections.singletonList(persistenteQuelle));

			List<Deskriptor> deskriptoren = getDeskriptorenForTest();

			when(deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren())).thenReturn(deskriptoren);

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, ""));

			// Assert
			assertEquals(1, result.size());
			QuelleReadonly quelle = result.get(0);
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService).mapToDeskriptoren(persistenteQuelle.getDeskriptoren());

			assertEquals(2, quelle.getDeskriptoren().size());
			assertEquals("q-uuid-3", quelle.getId());
			assertEquals("m-uuid-2", quelle.getMediumUuid());
			assertEquals("2x3 und Spaß dabei, S.42", quelle.getName());
			assertEquals(Quellenart.BUCH, quelle.getQuellenart());

		}
	}

	@Nested
	class SucheMitDeskriptorenTests {

		@Test
		void should_sucheQuellenReturnQuelle_when_einTrefferMitZeitschriftUndDeskriptorenTeilmenge() {

			// Arrange
			PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
			persistenteQuelle.setDeskriptoren("1,3,10");
			persistenteQuelle.setMediumTitel("alpha");
			persistenteQuelle.setQuellenart(Quellenart.ZEITSCHRIFT);
			persistenteQuelle.setJahrgang("1978");
			persistenteQuelle.setMediumUuid("m-uuid-1");
			persistenteQuelle.setAusgabe("3");
			persistenteQuelle.setSeite("13");
			persistenteQuelle.setSortNumber(1);
			persistenteQuelle.setUuid("q-uuid-2");

			String suchstring = "alpha";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(Collections.singletonList(persistenteQuelle));

			List<Deskriptor> deskriptoren = getDeskriptorenForTest();

			when(deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren())).thenReturn(deskriptoren);

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, "1,3"));

			// Assert
			assertEquals(1, result.size());
			QuelleReadonly quelle = result.get(0);
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService).mapToDeskriptoren(persistenteQuelle.getDeskriptoren());

			assertEquals(2, quelle.getDeskriptoren().size());
			assertEquals("q-uuid-2", quelle.getId());
			assertEquals("m-uuid-1", quelle.getMediumUuid());
			assertEquals("alpha (3) 1978, S.13", quelle.getName());
			assertEquals(Quellenart.ZEITSCHRIFT, quelle.getQuellenart());

		}

		@Test
		void should_sucheQuellenReturnEmptyList_when_einTrefferMitZeitschriftAberDeskriptorenKeineTeilmenge() {

			// Arrange
			PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
			persistenteQuelle.setDeskriptoren("1,3,10");
			persistenteQuelle.setMediumTitel("alpha");
			persistenteQuelle.setQuellenart(Quellenart.ZEITSCHRIFT);
			persistenteQuelle.setJahrgang("1978");
			persistenteQuelle.setMediumUuid("m-uuid-1");
			persistenteQuelle.setAusgabe("3");
			persistenteQuelle.setSeite("13");
			persistenteQuelle.setSortNumber(1);
			persistenteQuelle.setUuid("q-uuid-2");

			String suchstring = "alpha";
			when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
				.thenReturn(Collections.singletonList(persistenteQuelle));

			// Act
			List<QuelleReadonly> result = service.sucheQuellen(new Suchfilter(suchstring, "1,3,5"));

			// Assert
			assertEquals(0, result.size());
			verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
			verify(deskriptorenService, never()).mapToDeskriptoren(persistenteQuelle.getDeskriptoren());
		}
	}

	private List<Deskriptor> getDeskriptorenForTest() {

		List<Deskriptor> deskriptoren = new ArrayList<>();

		{

			Deskriptor deskriptor = new Deskriptor("Mathe", false, "RAETSEL");
			deskriptor.id = 1l;
			deskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Minikänguru", false, "RAETSEL");
			deskriptor.id = 3l;
			deskriptoren.add(deskriptor);
		}

		return deskriptoren;
	}
}
