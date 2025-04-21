// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;
import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.domain.deskriptoren.DeskriptorUI;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.InjectMock;
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
@TestProfile(FullDatabaseAdminTestProfile.class)
public class DeskriptorenResourceTest {

	@InjectMock
	AuthenticationContext authCtx;

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	public void testLoadDeskriptorenEndpointV2AsAdmin() throws Exception {

		AuthenticatedUser user = new AuthenticatedUser("abcdef-012345").withRoles(new String[] { "ADMIN" })
			.withBenutzerart(Benutzerart.ADMIN);
		when(authCtx.getUser()).thenReturn(user);

		Response response = given().headers("X-SESSIONID", "12345", "X-CORRELATION-ID", "98765", "X-CLIENT-ID", "quarkus-tests")
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(85, deskriptoren.length);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	public void testLoadDeskriptorenEndpointV2AsAutor() throws Exception {

		AuthenticatedUser user = new AuthenticatedUser("abcdef-012345").withRoles(new String[] { "AUTOR" })
			.withBenutzerart(Benutzerart.AUTOR);
		when(authCtx.getUser()).thenReturn(user);

		Response response = given().headers("X-SESSIONID", "12345", "X-CORRELATION-ID", "98765", "X-CLIENT-ID", "quarkus-tests")
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(85, deskriptoren.length);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	public void testLoadDeskriptorenEndpointV2AsOrdinaryUser() throws Exception {

		AuthenticatedUser user = new AuthenticatedUser("abcdef-012345").withRoles(new String[] { "STANDARD" })
			.withBenutzerart(Benutzerart.STANDARD);
		when(authCtx.getUser()).thenReturn(user);

		Response response = given().headers("X-SESSIONID", "12345", "X-CORRELATION-ID", "98765", "X-CLIENT-ID", "quarkus-tests")
			.when().get("v2");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();

		DeskriptorUI[] deskriptoren = new ObjectMapper().readValue(responsePayload, DeskriptorUI[].class);

		assertEquals(31, deskriptoren.length);
	}
}
