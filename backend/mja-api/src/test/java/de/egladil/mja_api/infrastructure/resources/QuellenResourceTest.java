// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.quellen.QuellenListItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

/**
 * QuellenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuellenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class QuellenResourceTest {

	@Test
	void testSucheOhneDeskriptorenMitTreffer() {

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[]}]";
		given()
			.when().get("v2?suchstring=Winkelvoß")
			.then()
			.statusCode(200)
			.body(is(expected));

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
	void testFindQuelleByIdMitTreffer() throws Exception {

		Response response = given()
			.when().get("v1/8ef4d9b8-62a6-4643-8674-73ebaec52d98");

		String responsePayload = response.asString();

		QuellenListItem quelle = new ObjectMapper().readValue(responsePayload, QuellenListItem.class);

		assertEquals("Heike Winkelvoß", quelle.getName());

		assertEquals(200, response.statusCode());

	}

	@Test
	void testFindQuelleByIdOhneTreffer() throws Exception {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Es gibt keine Quelle mit dieser UUID\"}";

		given()
			.when().get("v1/7a94e100-85e9-4ffb-903b-06835851063b")
			.then().statusCode(404).and()
			.body(is(expected));

	}

}
