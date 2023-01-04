// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class QuellenResourceV1Test {

	@Test
	void testSucheOhneDeskriptorenMitTreffer() {

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[]}]";
		given()
			.when().get("v1?suchstring=Winkelvoß")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenMitTreffer() {

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[]}]";
		given()
			.when().get("v1?suchstring=Heike&deskriptoren=40")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenOhneTreffer() {

		String expected = "[]";

		given()
			.when().get("v1?suchstring=2x3&deskriptoren=2,7")
			.then()
			// .statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidSuchstring() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findQuellenV1.suchstring\",\"message\":\"ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,\"}]}";

		given()
			.when().get("v1?deskriptoren=5,7&suchstring=<böse>")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidDeskriptoren() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findQuellenV1.deskriptoren\",\"message\":\"ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma\"}]}";
		given()
			.when().get("v1?deskriptoren=5,A,8&suchstring=2x3")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testFindQuelleAdminV1MitTreffer() throws Exception {

		Response response = given()
			.when().get("admin/v1");

		String responsePayload = response.asString();

		QuellenListItem quelle = new ObjectMapper().readValue(responsePayload, QuellenListItem.class);

		assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", quelle.getId());

		assertEquals(200, response.statusCode());

	}
}
