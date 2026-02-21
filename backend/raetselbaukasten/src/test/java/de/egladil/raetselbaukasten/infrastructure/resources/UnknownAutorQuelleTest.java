// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.resources;

import java.util.Optional;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;

import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.raetselbaukasten.profiles.FullDatabaseQuelleAutorTestProfile;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UnknownAutorQuelleTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuellenResource.class)
@TestProfile(FullDatabaseQuelleAutorTestProfile.class)
public class UnknownAutorQuelleTest {

    @Inject
    QuellenRepository quellenRepository;

    @Test
    @TestSecurity(user = "testUser", roles = { "AUTOR" })
    void should_getAuthenticatedUserAsQuelleCreateNewQuelle() {

        QuelleDto result = given()
                .when()
                .get("autor/v2")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .extract()
                .as(QuelleDto.class);

        assertEquals("Bilbo Beutlin", result.getPerson());
        assertEquals(Quellenart.PERSON, result.getQuellenart());
        assertNull(result.getMediumUuid());
        assertNull(result.getAusgabe());
        assertNull(result.getJahr());
        assertNull(result.getKlasse());
        assertNull(result.getPfad());

        String userId = "5d89c2e1-5d35-4e1b-b5a5-c56defd8ba43";
        Optional<PersistenteQuelleReadonly> optAusDB = quellenRepository.findQuelleWithUserId(userId);
        assertTrue(optAusDB.isPresent());

        PersistenteQuelleReadonly ausDB = optAusDB.get();
        assertEquals(result.getId(), ausDB.getUuid());
        assertEquals(userId, ausDB.getUserId());

    }

}
