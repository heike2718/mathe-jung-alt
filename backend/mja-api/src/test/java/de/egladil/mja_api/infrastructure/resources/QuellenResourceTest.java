// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quellen.QuellenListItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * QuellenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuellenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class QuellenResourceTest {

	@Test
	void testSucheMitSuchstringNoAuthorization() {

		given()
			.when()
			.queryParam("suchstring", "Winkelvoß")
			.get("v2")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testSucheMitSuchstringUnauthorized() {

		given()
			.when()
			.queryParam("suchstring", "Winkelvoß")
			.get("v2")
			.then()
			.statusCode(403);
	}

	@Test
	@TestSecurity(user = "testUser", roles = { "ADMIN" })
	void testSucheUserADMINMitTreffer() {

		QuellenListItem[] result = given()
			.when()
			.queryParam("suchstring", "Winkelvoß")
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(QuellenListItem[].class);

		assertEquals("Heike Winkelvoß", result[0].getName());

	}

	@Test
	@TestSecurity(user = "testUser", roles = { "AUTOR" })
	void testSucheUserAUTORMitTreffer() {

		QuellenListItem[] result = given()
			.when()
			.queryParam("suchstring", "Winkelvoß")
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(QuellenListItem[].class);

		assertEquals("Heike Winkelvoß", result[0].getName());

	}

	@Test
	void testSucheMitInvalidSuchstring() {

		try {

			given()
				.when().get("v2?&suchstring=bös'");

			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {

			System.out.println(e.getMessage());
		}

	}

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
	void testFindQuelleByIdMitTreffer() throws Exception {

		// Arrange
		String quelleId = "8ef4d9b8-62a6-4643-8674-73ebaec52d98";

		QuellenListItem responsePayload = given()
			.when().get(quelleId + "/v1").then().statusCode(200).and().extract().as(QuellenListItem.class);

		assertEquals("Heike Winkelvoß", responsePayload.getName());
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

}
