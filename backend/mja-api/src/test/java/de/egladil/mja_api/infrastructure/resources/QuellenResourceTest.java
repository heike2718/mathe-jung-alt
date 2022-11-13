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

import de.egladil.mja_api.domain.quellen.QuelleReadonly;
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

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[{\"id\":40,\"name\":\"Person\",\"admin\":true,\"kontext\":\"QUELLEN\"}]}]";
		given()
			.when().get("?suchstring=Winkelvoß")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenMitTreffer() {

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[{\"id\":40,\"name\":\"Person\",\"admin\":true,\"kontext\":\"QUELLEN\"}]}]";

		given()
			.when().get("?suchstring=Heike&deskriptoren=40")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenOhneTreffer() {

		String expected = "[]";

		given()
			.when().get("?suchstring=2x3&deskriptoren=2,7")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidSuchstring() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findQuellen.suchstring\",\"message\":\"ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,\"}]}";

		given()
			.when().get("?deskriptoren=5,7&suchstring=<böse>")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidDeskriptoren() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findQuellen.deskriptoren\",\"message\":\"ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma\"}]}";

		given()
			.when().get("?deskriptoren=5,A,8&suchstring=2x3")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testFindQuelleByPersonMitTreffer() throws Exception {

		Response response = given()
			.when().get("admin");

		String responsePayload = response.asString();

		QuelleReadonly quelle = new ObjectMapper().readValue(responsePayload, QuelleReadonly.class);

		assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", quelle.getId());

		assertEquals(200, response.statusCode());

	}

	@Test
	void testFindQuelleByIdMitTreffer() throws Exception {

		Response response = given()
			.when().get("8ef4d9b8-62a6-4643-8674-73ebaec52d98");

		String responsePayload = response.asString();

		QuelleReadonly quelle = new ObjectMapper().readValue(responsePayload, QuelleReadonly.class);

		assertEquals("Heike Winkelvoß", quelle.getName());

		assertEquals(200, response.statusCode());

	}

	@Test
	void testFindQuelleByIdOhneTreffer() throws Exception {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Es gibt keine Quelle mit dieser UUID\"}";

		given()
			.when().get("7a94e100-85e9-4ffb-903b-06835851063b")
			.then()
			.body(is(expected))
			.statusCode(404);

	}

}
