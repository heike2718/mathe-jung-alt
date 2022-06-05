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
			.body(is("2"));
	}

	@Test
	void testZaehleRaetselMitSuchstring() {

		given()
			.when().get("size?suchstring=Minikänguru&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("13"));
	}

	@Test
	void testZaehleRaetselMitGenauEinemDeskriptorTypString() {

		given()
			.when().get("size?deskriptoren=EINS&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("9"));
	}

	@Test
	void testZaehleRaetselMitGenauEinemDeskriptorTypOrdinal() {

		given()
			.when().get("size?deskriptoren=8&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(200)
			.body(is("6"));
	}

	@Test
	void testFindRaetselMitDeskriptorenUndSuchstring() throws Exception {

		Response response = given()
			.when().get("?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING");
		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer[] alleRaetsel = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer[].class);
		assertEquals(2, alleRaetsel.length);

		{

			RaetselsucheTreffer raetsel = alleRaetsel[0];
			assertEquals("7a94e100-85e9-4ffb-903b-06835851063b", raetsel.getId());
			assertEquals("02789", raetsel.getSchluessel());
		}
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
		assertEquals(3, alleRaetsel.length);

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
	}

	@Test
	void testFindRaetselMitSuchstring() throws Exception {

		Response response = given()
			.when().get("?suchstring=zählen&typeDeskriptoren=ORDINAL");
		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer[] alleRaetsel = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer[].class);

		assertEquals(5, alleRaetsel.length);

		{

			RaetselsucheTreffer raetsel = alleRaetsel[0];
			assertEquals("57d53a52-9609-46b2-bbfb-7e3d9e1983b5", raetsel.getId());
			assertEquals("02596", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[1];
			assertEquals("f3b70e16-c431-42b7-b919-751de708d9d7", raetsel.getId());
			assertEquals("02777", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[2];
			assertEquals("7a94e100-85e9-4ffb-903b-06835851063b", raetsel.getId());
			assertEquals("02789", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[3];
			assertEquals("a18315fc-ed01-45c3-bf2d-078dd1fa47f4", raetsel.getId());
			assertEquals("02791", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[3];
			assertEquals("024f4ca4-3235-48a4-9c88-e77990ea059c", raetsel.getId());
			assertEquals("02816", raetsel.getSchluessel());
		}
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
