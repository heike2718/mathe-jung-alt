// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
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
	void testFindQuelleByIdUnauthorized() throws Exception {

		given()
			.when()
			.get("8ef4d9b8-62a6-4643-8674-73ebaec52d98/v1")
			.then()
			.statusCode(401);

	}

	@Test
	@TestSecurity(user = "testUser", roles = { "STANDARD" })
	void testFindQuelleByIdForbidden() throws Exception {

		given()
			.when()
			.get("8ef4d9b8-62a6-4643-8674-73ebaec52d98/v1")
			.then()
			.statusCode(403);

	}

	@Test
	@TestSecurity(user = "testUser", roles = { "ADMIN" })
	void testFindQuelleByIdADMINOhneTreffer() throws Exception {

		MessagePayload messagePayload = given()
			.when().get("7a94e100-85e9-4ffb-903b-06835851063b/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("Es gibt keine Quelle mit dieser UUID", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testUser", roles = { "AUTOR" })
	void testFindQuelleByIdAUTOROhneTreffer() throws Exception {

		MessagePayload messagePayload = given()
			.when().get("7a94e100-85e9-4ffb-903b-06835851063b/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("Es gibt keine Quelle mit dieser UUID", messagePayload.getMessage());

	}

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
