// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_api.profiles.ContainerDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

/**
 * DeskriptorenResourceContainerizedTest
 */
@QuarkusTest
@TestHTTPEndpoint(DeskriptorenResource.class)
@TestProfile(ContainerDatabaseTestProfile.class)
public class DeskriptorenResourceContainerizedTest {

	@Test
	public void testLoadDeskriptorenEndpoint() throws Exception {

		Response response = given()
			.when().get();

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Deskriptor[] deskriptoren = new ObjectMapper().readValue(responsePayload, Deskriptor[].class);

		assertEquals(7, deskriptoren.length);

		response
			.then()
			.statusCode(200);

		int anzahlAdmin = 0;
		int anzahlRAETSEL = 0;

		for (Deskriptor d : deskriptoren) {

			if (d.admin) {

				anzahlAdmin++;
			}

			if (d.kontext.equals("RAETSEL")) {

				anzahlRAETSEL++;
			}
		}

		assertEquals(0, anzahlAdmin);
		assertEquals(7, anzahlRAETSEL);

	}

	@Test
	public void testLoadDeskriptorenEndpoint_ignoresQueryParameter() throws Exception {

		Response response = given()
			.when().get("?kontext=NOOP");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Deskriptor[] deskriptoren = new ObjectMapper().readValue(responsePayload, Deskriptor[].class);

		assertEquals(7, deskriptoren.length);

		response
			.then()
			.statusCode(200);

		int anzahlAdmin = 0;
		int anzahlRAETSEL = 0;

		for (Deskriptor d : deskriptoren) {

			if (d.admin) {

				anzahlAdmin++;
			}

			if (d.kontext.equals("RAETSEL")) {

				anzahlRAETSEL++;
			}
		}

		assertEquals(0, anzahlAdmin);
		assertEquals(7, anzahlRAETSEL);

	}
}
