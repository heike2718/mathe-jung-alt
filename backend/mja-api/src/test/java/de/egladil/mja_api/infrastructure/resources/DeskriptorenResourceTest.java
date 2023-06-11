// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorUI;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;

/**
 * DeskriptorenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(DeskriptorenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class DeskriptorenResourceTest {

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	public void testLoadDeskriptorenEndpointV2AsAdmin() throws Exception {

		Response response = given()
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(82, deskriptoren.length);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	public void testLoadDeskriptorenEndpointV2AsAutor() throws Exception {

		Response response = given()
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(82, deskriptoren.length);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	public void testLoadDeskriptorenEndpointV2AsOrdinaryUser() throws Exception {

		Response response = given()
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(27, deskriptoren.length);
	}
}
