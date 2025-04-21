// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;
import de.egladil.raetselbaukasten.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AdminQuellenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuellenResource.class)
@TestProfile(FullDatabaseAdminTestProfile.class)
public class AdminQuellenResourceTest {

	@Test
	@TestSecurity(user = "testUser", roles = { "AUTOR" })
	void testGetAuthenticatedUserAsQuelle() {

		QuelleDto result = given()
			.when()
			.get("autor/v2")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(QuelleDto.class);

		assertEquals("Heike Winkelvoß", result.getPerson());
		assertEquals(Quellenart.PERSON, result.getQuellenart());
		assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", result.getId());
		assertNull(result.getMediumUuid());
		assertNull(result.getAusgabe());
		assertNull(result.getJahr());
		assertNull(result.getKlasse());
		assertNull(result.getPfad());
	}

}
