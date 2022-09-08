// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.resources.DeskriptorenResource;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

/**
 * DeskriptorenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(DeskriptorenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class DeskriptorenResourceTest {

	@Test
	public void testLoadDeskriptorenEndpoint() throws Exception {

		Response response = given()
			.when().get("?kontext=RAETSEL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Deskriptor[] deskriptoren = new ObjectMapper().readValue(responsePayload, Deskriptor[].class);

		assertEquals(81, deskriptoren.length);

		response
			.then()
			.statusCode(200);

		int anzahlAdmin = 0;

		for (Deskriptor d : deskriptoren) {

			if (d.admin) {

				anzahlAdmin++;
			}
		}

		assertEquals(54, anzahlAdmin);

	}

}
