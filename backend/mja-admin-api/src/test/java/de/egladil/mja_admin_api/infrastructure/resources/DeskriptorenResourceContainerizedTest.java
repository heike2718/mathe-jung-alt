// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_admin_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_admin_api.profiles.ContainerDatabaseTestProfile;
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
			.when().get("?kontext=RAETSEL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Deskriptor[] deskriptoren = new ObjectMapper().readValue(responsePayload, Deskriptor[].class);

		assertEquals(15, deskriptoren.length);

		response
			.then()
			.statusCode(200);

		int anzahlAdmin = 0;

		for (Deskriptor d : deskriptoren) {

			if (d.admin) {

				anzahlAdmin++;
			}
		}

		assertEquals(8, anzahlAdmin);

	}

}
