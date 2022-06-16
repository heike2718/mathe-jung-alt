// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_admin_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

/**
 * RaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class RaetselResourceTest {

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
			.body(is("16"));
	}

	@Test
	void testZaehleRaetselMitGenauEinemDeskriptorTypString() {

		given()
			.when().get("size?deskriptoren=EINS&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("11"));
	}

	@Test
	void testZaehleRaetselMitGenauEinemDeskriptorTypOrdinal() {

		given()
			.when().get("size?deskriptoren=8&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(200)
			.body(is("7"));
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

		assertEquals(6, alleRaetsel.length);

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
			assertEquals("1267285d-f781-42e1-b0e6-7b46ef2e85b2", raetsel.getId());
			assertEquals("02790", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[4];
			assertEquals("a18315fc-ed01-45c3-bf2d-078dd1fa47f4", raetsel.getId());
			assertEquals("02791", raetsel.getSchluessel());
		}

		{

			RaetselsucheTreffer raetsel = alleRaetsel[5];
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

	@Test
	void testFindFromMinikaenguruWithCoordinates() throws Exception {

		Response response = given().when().get("?deskriptoren=2,6,47,78&typeDeskriptoren=ORDINAL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);
		RaetselsucheTreffer[] alleRaetsel = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer[].class);

		assertEquals(1, alleRaetsel.length);

		RaetselsucheTreffer treffer = alleRaetsel[0];
		assertEquals("02790", treffer.getSchluessel());

	}

	@Test
	void testFindWithSchluessel() throws Exception {

		Response response = given().when().get("?suchstring=02790&typeDeskriptoren=ORDINAL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);
		RaetselsucheTreffer[] alleRaetsel = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer[].class);

		assertEquals(1, alleRaetsel.length);

		RaetselsucheTreffer treffer = alleRaetsel[0];
		assertEquals("02790", treffer.getSchluessel());

	}
}
