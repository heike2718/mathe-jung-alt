// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * RaetselgruppenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselgruppenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class RaetselgruppenResourceAuthorizationTest {

	/**
	 *
	 */
	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	void testFindRaetselgruppenUnauthorized() throws Exception {

		given()
			.contentType(ContentType.JSON)
			.get(
				"v1?limit=20&offset=0&referenz=2022&referenztyp=MINIKAENGURU&schwierigkeitsgrad=EINS&sortAttribute=name&sortDirection=asc")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindRaetselgruppenForbidden() throws Exception {

		given()
			.contentType(ContentType.JSON)
			.get(
				"v1?limit=20&offset=0&referenz=2022&referenztyp=MINIKAENGURU&schwierigkeitsgrad=EINS&sortAttribute=name&sortDirection=asc")
			.then()
			.statusCode(403);
	}

	@Test
	void testLoadDetailsUnauthorized() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.get("v1/" + id)
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "LEHRER" })
	void testLoadDetailsForbidden() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		given()
			.contentType(ContentType.JSON)
			.get("v1/" + id)
			.then()
			.assertThat()
			.statusCode(403);
	}

	@Test
	void testRaetselgruppeAnlegenUnauthorized() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.post("v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "LEHRER" })
	void testRaetselgruppeAnlegenForbidden() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.post("v1")
			.then()
			.statusCode(403);
	}

	@Test
	void testRaetselgruppeAendernUnauthorized() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.put("v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "LEHRER" })
	void testRaetselgruppeAendernForbidden() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.put("v1")
			.then()
			.statusCode(403);
	}

	@Test
	void raetselgruppenelementLoeschenUnauthorized() throws Exception {

		String raetselgruppeUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete("v1/" + raetselgruppeUuid + "/elemente/abcdef")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void raetselgruppenelementLoeschenForbidden() throws Exception {

		String raetselgruppeUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete("v1/" + raetselgruppeUuid + "/elemente/abcdef")
			.then()
			.statusCode(403);
	}

	@Test
	void elementAnlegenUnauthorized() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
		payload.setId("neu");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.post("v1/abcdef-987654/elemente")
			.then()
			.statusCode(401);

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void elementAnlegenForbidden() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
		payload.setId("neu");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		String requestBody = new ObjectMapper().writeValueAsString(payload);

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(requestBody)
			.post("v1/abcdef-987654/elemente")
			.then()
			.statusCode(403);

	}

	@Test
	void testGetLaTeXUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"v1/latex/10257506-71c5-423e-b355-bf90b5bb344d?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testGetLaTeXForbidden() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"v1/latex/10257506-71c5-423e-b355-bf90b5bb344d?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(403);
	}

	@Test
	void testGetVorschauUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"v1/vorschau/10257506-71c5-423e-b355-bf90b5bb344d?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testGetVorschauForbidden() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"v1/vorschau/10257506-71c5-423e-b355-bf90b5bb344d?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(403);
	}

}
