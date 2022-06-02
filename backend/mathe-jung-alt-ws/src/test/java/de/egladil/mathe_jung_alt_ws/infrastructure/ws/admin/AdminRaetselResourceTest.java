// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mathe_jung_alt_ws.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

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
			.body(is("3"));
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
	void testFindRaetselMitDeskriptorenNumerischUndSuchstring() throws Exception {

		// Volltextsuche unterscheidet nicht zwischen zahlen und zählen!

		Response response = given()
			.when().get("?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer[] alleRaetsel = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer[].class);
		assertEquals(2, alleRaetsel.length);

		{

			RaetselsucheTreffer raetsel = alleRaetsel[0];
			assertEquals("f3b70e16-c431-42b7-b919-751de708d9d7", raetsel.getId());
			assertEquals("02777", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[1];
			assertEquals("7a94e100-85e9-4ffb-903b-06835851063b", raetsel.getId());
			assertEquals("02789", raetsel.getSchluessel());
		}

		assertEquals(200, response.getStatusCode());
	}

	@Test
	void testFindRaetselMitSuchstring() {

		String expected = "[{\"id\":\"f3b70e16-c431-42b7-b919-751de708d9d7\",\"schluessel\":\"02777\",\"name\":\"Domino mit Waggons\",\"kommentar\":\"Minikänguru 2022 EINS ZWEI\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":8,\"name\":\"ZWEI\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Vorschule\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":47,\"name\":\"A-2\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":48,\"name\":\"A-3\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":78,\"name\":\"2022\",\"admin\":true,\"kontext\":\"RAETSEL\"}]},{\"id\":\"7a94e100-85e9-4ffb-903b-06835851063b\",\"schluessel\":\"02789\",\"name\":\"2022 zählen\",\"kommentar\":\"Minikänguru 2022 Klasse 1\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Vorschule\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":46,\"name\":\"A-1\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":78,\"name\":\"2022\",\"admin\":true,\"kontext\":\"RAETSEL\"}]}]";
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
