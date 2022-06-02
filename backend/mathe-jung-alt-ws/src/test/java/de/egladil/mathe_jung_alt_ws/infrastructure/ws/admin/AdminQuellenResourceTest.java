// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import de.egladil.mathe_jung_alt_ws.profiles.ContainerDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * AdminQuellenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AdminQuellenResource.class)
@TestProfile(ContainerDatabaseTestProfile.class)
public class AdminQuellenResourceTest {

	@Test
	void testSucheOhneDeskriptorenMitTreffer() {

		String expected = "[{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"sortNumber\":1,\"name\":\"Heike Winkelvoß\",\"mediumUuid\":null,\"deskriptoren\":[{\"id\":40,\"name\":\"Person\",\"admin\":true,\"kontext\":\"QUELLEN\"}]}]";
		given()
			.when().get("?suchstring=Heike")
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

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"sucheQuellen.suchstring\",\"message\":\"ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,\"}]}";

		given()
			.when().get("?deskriptoren=5,7&suchstring=<böse>")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidDeskriptoren() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"sucheQuellen.deskriptoren\",\"message\":\"ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma\"}]}";
		given()
			.when().get("?deskriptoren=5,A,8&suchstring=2x3")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

}
