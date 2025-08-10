// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.raetselbaukasten.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * QuellenServiceTest
 */
@QuarkusTest
@TestProfile(FullDatabaseAdminTestProfile.class)
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
        persistenteQuelle.setPerson("Heike Winkelvoß");
        persistenteQuelle.setUserId("u-uuid-1");
        persistenteQuelle.setQuellenart(Quellenart.PERSON);
        persistenteQuelle.setSortNumber(1);
        persistenteQuelle.setUuid(uuid);

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
        persistenteQuelle.setQuellenart(Quellenart.ZEITSCHRIFT);
        persistenteQuelle.setJahr("1978");
        persistenteQuelle.setMediumUuid("m-uuid-1");
        persistenteQuelle.setAusgabe("3");
        persistenteQuelle.setSeite("13");
        persistenteQuelle.setSortNumber(1);
        persistenteQuelle.setUuid(uuid);

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
        persistenteQuelle.setMediumUuid("m-uuid-2");
        persistenteQuelle.setQuellenart(Quellenart.BUCH);
        persistenteQuelle.setSeite("42");
        persistenteQuelle.setSortNumber(1);
        persistenteQuelle.setUuid(uuid);

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
        persistenteQuelle.setJahr("2013");
        persistenteQuelle.setMediumUuid("m-uuid-4");
        persistenteQuelle.setQuellenart(Quellenart.INTERNET);
        persistenteQuelle.setKlasse("Klasse 1");
        persistenteQuelle.setStufe("Stufe 2");
        persistenteQuelle.setUuid(uuid);

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
