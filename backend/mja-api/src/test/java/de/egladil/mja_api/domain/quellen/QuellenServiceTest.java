// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
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
	void should_getQuelleWithId_work_whenPerson() {

		// Arrange
		String uuid = "q-uuid-1";

		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.person = "Heike Winkelvoß";
		persistenteQuelle.userId = "u-uuid-1";
		persistenteQuelle.quellenart = Quellenart.PERSON;
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = uuid;

		when(quellenRepository.findQuelleReadonlyById(uuid)).thenReturn(persistenteQuelle);

		// Act
		Optional<QuelleDto> optQuelle = service.getQuelleWithId(uuid);

		// Assert
		verify(quellenRepository).findQuelleReadonlyById(uuid);

		QuelleDto quelle = optQuelle.get();

		assertEquals(uuid, quelle.getId());
		assertNull(quelle.getMediumUuid());
		assertEquals("Heike Winkelvoß", quelle.getPerson());
		assertEquals(Quellenart.PERSON, quelle.getQuellenart());
		assertNull(quelle.getAusgabe());
		assertNull(quelle.getJahr());
		assertNull(quelle.getKlasse());
		assertNull(quelle.getMediumUuid());
		assertNull(quelle.getSeite());
		assertNull(quelle.getStufe());
	}

	@Test
	void should_getQuelleWithId_work_whenZeitschrift() {

		// Arrange
		String uuid = "q-uuid-2";

		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.quellenart = Quellenart.ZEITSCHRIFT;
		persistenteQuelle.jahr = "1978";
		persistenteQuelle.mediumUuid = "m-uuid-1";
		persistenteQuelle.ausgabe = "3";
		persistenteQuelle.seite = "13";
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = uuid;

		when(quellenRepository.findQuelleReadonlyById(uuid)).thenReturn(persistenteQuelle);

		// Act
		Optional<QuelleDto> optQuelle = service.getQuelleWithId(uuid);

		// Assert
		verify(quellenRepository).findQuelleReadonlyById(uuid);

		QuelleDto quelle = optQuelle.get();

		assertEquals(uuid, quelle.getId());
		assertEquals("m-uuid-1", quelle.getMediumUuid());
		assertNull(quelle.getPerson());
		assertEquals(Quellenart.ZEITSCHRIFT, quelle.getQuellenart());
		assertEquals("3", quelle.getAusgabe());
		assertEquals("1978", quelle.getJahr());
		assertNull(quelle.getKlasse());
		assertEquals("13", quelle.getSeite());
		assertNull(quelle.getStufe());

	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferMitBuch() {

		// Arrange
		String uuid = "q-uuid-3";

		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.mediumUuid = "m-uuid-2";
		persistenteQuelle.quellenart = Quellenart.BUCH;
		persistenteQuelle.seite = "42";
		persistenteQuelle.sortNumber = 1;
		persistenteQuelle.uuid = uuid;

		when(quellenRepository.findQuelleReadonlyById(uuid)).thenReturn(persistenteQuelle);

		// Act
		Optional<QuelleDto> optQuelle = service.getQuelleWithId(uuid);

		// Assert
		verify(quellenRepository).findQuelleReadonlyById(uuid);

		QuelleDto quelle = optQuelle.get();

		assertEquals(uuid, quelle.getId());
		assertEquals("m-uuid-2", quelle.getMediumUuid());
		assertNull(quelle.getPerson());
		assertEquals(Quellenart.BUCH, quelle.getQuellenart());
		assertNull(quelle.getAusgabe());
		assertNull(quelle.getJahr());
		assertNull(quelle.getKlasse());
		assertEquals("42", quelle.getSeite());
		assertNull(quelle.getStufe());
	}

	@Test
	void should_findQuellenReturnQuelle_when_einTrefferInternetVollstaendig() {

		// Arrange
		String uuid = "q-uuid-4";

		PersistenteQuelleReadonly persistenteQuelle = new PersistenteQuelleReadonly();
		persistenteQuelle.jahr = "2013";
		persistenteQuelle.mediumUuid = "m-uuid-4";
		persistenteQuelle.quellenart = Quellenart.INTERNET;
		persistenteQuelle.klasse = "Klasse 1";
		persistenteQuelle.stufe = "Stufe 2";
		persistenteQuelle.uuid = uuid;

		when(quellenRepository.findQuelleReadonlyById(uuid)).thenReturn(persistenteQuelle);

		// Act
		Optional<QuelleDto> optQuelle = service.getQuelleWithId(uuid);

		// Assert
		verify(quellenRepository).findQuelleReadonlyById(uuid);

		QuelleDto quelle = optQuelle.get();

		assertEquals(uuid, quelle.getId());
		assertEquals("m-uuid-4", quelle.getMediumUuid());
		assertNull(quelle.getPerson());
		assertEquals(Quellenart.INTERNET, quelle.getQuellenart());
		assertNull(quelle.getAusgabe());
		assertEquals("2013", quelle.getJahr());
		assertEquals("Klasse 1", quelle.getKlasse());
		assertEquals("Stufe 2", quelle.getStufe());
		assertNull(quelle.getSeite());

	}
}
