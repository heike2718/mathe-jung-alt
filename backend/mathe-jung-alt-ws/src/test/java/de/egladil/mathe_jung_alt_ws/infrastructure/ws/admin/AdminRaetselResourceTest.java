// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import de.egladil.mathe_jung_alt_ws.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * AdminRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AdminRaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class AdminRaetselResourceTest {

	@Test
	void testZaehleRaetselMitDeskriptorenUndSuchstring() {

		given()
			.when().get("size?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("1"));
	}

	@Test
	void testZaehleRaetselMitSuchstring() {

		given()
			.when().get("size?suchstring=Minikänguru&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("2"));
	}

	@Test
	void testFindRaetselMitDeskriptorenUndSuchstring() {

		String expected = "[{\"id\":\"7a94e100-85e9-4ffb-903b-06835851063b\",\"schluessel\":\"02789\",\"name\":\"2022 zählen\",\"kommentar\":\"Minikänguru 2022 Klasse 1\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Vorschule\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":46,\"name\":\"A-1\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":78,\"name\":\"2022\",\"admin\":true,\"kontext\":\"RAETSEL\"}]}]";

		given()
			.when().get("?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	void testFindRaetselMitDeskriptorenNumerischUndSuchstring() {

		String expected = "[{\"id\":\"7a94e100-85e9-4ffb-903b-06835851063b\",\"schluessel\":\"02789\",\"name\":\"2022 zählen\",\"kommentar\":\"Minikänguru 2022 Klasse 1\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Vorschule\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":46,\"name\":\"A-1\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":78,\"name\":\"2022\",\"admin\":true,\"kontext\":\"RAETSEL\"}]}]";

		given()
			.when().get("?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	void testFindRaetselMitSuchstring() {

		String expected = "[{\"id\":\"7a94e100-85e9-4ffb-903b-06835851063b\",\"schluessel\":\"02789\",\"name\":\"2022 zählen\",\"kommentar\":\"Minikänguru 2022 Klasse 1\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Vorschule\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":46,\"name\":\"A-1\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":78,\"name\":\"2022\",\"admin\":true,\"kontext\":\"RAETSEL\"}]}]";

		given()
			.when().get("?suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	void testFindRaetselMitSuchstringKeinTreffer() {

		String expected = "[]";

		given()
			.when().get("?suchstring=holleriedidudeldö&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

}
