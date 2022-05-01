// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * AdminQuellenResourceTest
 */
@QuarkusTest
public class AdminQuellenResourceTest {

	@Test
	void testSucheOhneDeskriptorenMitTreffer() {

		String expected = "[{\"uuid\":\"8ef4d9b8\",\"quellenart\":\"BUCH\",\"sortNumber\":1,\"name\":\"Johannes Lehmann: 2x3 Plus Spaß dabei, S.43\",\"mediumUuid\":\"cd69615e\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false},{\"id\":6,\"name\":\"IKID\",\"admin\":false},{\"id\":7,\"name\":\"EINS\",\"admin\":false},{\"id\":8,\"name\":\"ZWEI\",\"admin\":false},{\"id\":15,\"name\":\"Buch\",\"admin\":false}]}]";

		given()
			.when().get("/admin/quellen?suchstring=2x3")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenMitTreffer() {

		String expected = "[{\"uuid\":\"8ef4d9b8\",\"quellenart\":\"BUCH\",\"sortNumber\":1,\"name\":\"Johannes Lehmann: 2x3 Plus Spaß dabei, S.43\",\"mediumUuid\":\"cd69615e\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false},{\"id\":6,\"name\":\"IKID\",\"admin\":false},{\"id\":7,\"name\":\"EINS\",\"admin\":false},{\"id\":8,\"name\":\"ZWEI\",\"admin\":false},{\"id\":15,\"name\":\"Buch\",\"admin\":false}]}]";

		given()
			.when().get("/admin/quellen?suchstring=2x3&deskriptoren=5,7")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitDeskriptorenOhneTreffer() {

		String expected = "[]";

		given()
			.when().get("/admin/quellen?suchstring=2x3&deskriptoren=2,7")
			.then()
			.statusCode(200)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidSuchstring() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findeQuellen.suchstring\",\"message\":\"ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,\"}]}";
		given()
			.when().get("/admin/quellen?deskriptoren=5,7&suchstring=<böse>")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

	@Test
	void testSucheMitInvalidDeskriptoren() {

		String expected = "{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"findeQuellen.deskriptoren\",\"message\":\"ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma\"}]}";

		given()
			.when().get("/admin/quellen?deskriptoren=5,A,8&suchstring=2x3")
			.then()
			.statusCode(400)
			.body(is(expected));

	}

}
