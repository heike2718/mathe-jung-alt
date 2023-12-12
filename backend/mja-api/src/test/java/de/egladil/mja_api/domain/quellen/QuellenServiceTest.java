// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

/**
 * QuellenServiceTest
 */
@QuarkusTest
@TestProfile(FullDatabaseTestProfile.class)
public class QuellenServiceTest {

	@InjectMock
	QuellenRepository quellenRepository;

	@Inject
	QuellenService service;

	@Test
	void should_findQuellenReturnThrowIllegalArgumentException_when_parameterBlank() {

		// Arrange
		String suchstring = "  ";

		// Act
		try {

			service.findQuellen(suchstring);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {

			assertEquals("suchstring erforderlich", e.getMessage());
		}

		// Assert
		verify(quellenRepository, never()).findQuellenLikeMediumOrPerson(suchstring);
	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferMitPerson() {

		// Arrange
		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.person = "Heike Winkelvoß";
		persistenteQuelle.quellenart = Quellenart.PERSON;
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = "q-uuid-1";

		String suchstring = "Winkel";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(Collections.singletonList(persistenteQuelle));

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(1, result.size());
		QuellenListItem quelle = result.get(0);
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);

		assertEquals(0, quelle.getDeskriptoren().size());
		assertEquals("q-uuid-1", quelle.getId());
		assertNull(quelle.getMediumUuid());
		assertEquals("Heike Winkelvoß", quelle.getName());
		assertEquals(Quellenart.PERSON, quelle.getQuellenart());

	}

	@Test
	void should_findQuellenReturnEmptyList_when_treffermengeLeer() {

		// Arrange
		String suchstring = "Winkel";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(new ArrayList<>());

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(0, result.size());
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
	}

	@Test
	void should_findQuellenReturnEmptyList_when_treffermengeNull() {

		// Arrange
		String suchstring = "Winkel";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(null);

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(0, result.size());
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);
	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferMitZeitschriftUndSeitennummer() {

		// Arrange
		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.mediumTitel = "alpha";
		persistenteQuelle.quellenart = Quellenart.ZEITSCHRIFT;
		persistenteQuelle.jahr = "1978";
		persistenteQuelle.mediumUuid = "m-uuid-1";
		persistenteQuelle.ausgabe = "3";
		persistenteQuelle.seite = "13";
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = "q-uuid-2";

		String suchstring = "alpha";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(Collections.singletonList(persistenteQuelle));

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(1, result.size());
		QuellenListItem quelle = result.get(0);
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);

		assertEquals(0, quelle.getDeskriptoren().size());
		assertEquals("q-uuid-2", quelle.getId());
		assertEquals("m-uuid-1", quelle.getMediumUuid());
		assertEquals("alpha (3) 1978, S.13", quelle.getName());
		assertEquals(Quellenart.ZEITSCHRIFT, quelle.getQuellenart());

	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferMitBuch() {

		// Arrange
		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.mediumTitel = "2x3 und Spaß dabei";
		persistenteQuelle.autor = "Johannes Lehmann";
		persistenteQuelle.mediumUuid = "m-uuid-2";
		persistenteQuelle.quellenart = Quellenart.BUCH;
		persistenteQuelle.seite = "42";
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = "q-uuid-3";

		String suchstring = "2x3";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(Collections.singletonList(persistenteQuelle));

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(1, result.size());
		QuellenListItem quelle = result.get(0);
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);

		assertEquals(0, quelle.getDeskriptoren().size());
		assertEquals("q-uuid-3", quelle.getId());
		assertEquals("m-uuid-2", quelle.getMediumUuid());
		assertEquals("Johannes Lehmann: 2x3 und Spaß dabei, S.42", quelle.getName());
		assertEquals(Quellenart.BUCH, quelle.getQuellenart());

	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferInternetVollstaendig() {

		// Arrange
		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.mediumTitel = "Herbstolympiade Russland";
		persistenteQuelle.jahr = "2013";
		persistenteQuelle.mediumUuid = "m-uuid-4";
		persistenteQuelle.quellenart = Quellenart.INTERNET;
		persistenteQuelle.klasse = "Klasse 1";
		persistenteQuelle.stufe = "Stufe 2";
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = "q-uuid-4";

		String suchstring = "Herbstolympiade";
		when(quellenRepository.findQuellenLikeMediumOrPerson(suchstring))
			.thenReturn(Collections.singletonList(persistenteQuelle));

		// Act
		List<QuellenListItem> result = service.findQuellen(suchstring);

		// Assert
		assertEquals(1, result.size());
		QuellenListItem quelle = result.get(0);
		verify(quellenRepository).findQuellenLikeMediumOrPerson(suchstring);

		assertEquals(0, quelle.getDeskriptoren().size());
		assertEquals("q-uuid-4", quelle.getId());
		assertEquals("m-uuid-4", quelle.getMediumUuid());
		assertEquals("Herbstolympiade Russland (2013), Klasse 1, Stufe 2", quelle.getName());
		assertEquals(Quellenart.INTERNET, quelle.getQuellenart());

	}
}
