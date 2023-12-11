// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungPayload;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungselementPayload;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AufgabensammlungenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AufgabensammlungenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class AufgabensammlungenResourceAuthorizationTest {

	/**
	 *
	 */
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	/**
	 *
	 */
	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	void testFindAufgabensammlungenUnauthorized() throws Exception {

		given()
			.contentType(ContentType.JSON)
			.get(
				"v1?limit=20&offset=0&referenz=2022&referenztyp=MINIKAENGURU&schwierigkeitsgrad=EINS&sortAttribute=name&sortDirection=asc")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungenForbidden() throws Exception {

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
			.get(id + "/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "LEHRER" })
	void testLoadDetailsForbidden() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		given()
			.contentType(ContentType.JSON)
			.get(id + "/v1")
			.then()
			.assertThat()
			.statusCode(403);
	}

	@Test
	void testAufgabensammlungAnlegenUnauthorized() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setFreigegeben(false);

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
	void testAufgabensammlungAnlegenForbidden() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setFreigegeben(false);

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
	void testAufgabensammlungAendernUnauthorized() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setFreigegeben(false);

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
	void testAufgabensammlungAendernForbidden() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setFreigegeben(false);

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
	void elementLoeschenUnauthorized() throws Exception {

		String aufgabensammlungUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete(aufgabensammlungUuid + "/elemente/abcdef/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void elementLoeschenForbidden() throws Exception {

		String aufgabensammlungUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete(aufgabensammlungUuid + "/elemente/abcdef/v1")
			.then()
			.statusCode(403);
	}

	@Test
	void elementAnlegenUnauthorized() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
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
			.post("abcdef-987654/elemente/v1")
			.then()
			.statusCode(401);

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void elementAnlegenForbidden() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
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
			.post("abcdef-987654/elemente/v1")
			.then()
			.statusCode(403);

	}

	@Test
	void test_downloadLaTeXUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(APPLICATION_OCTET_STREAM)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/latex/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_downloadLaTeXForbidden() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(APPLICATION_OCTET_STREAM)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/latex/v1")
			.then()
			.statusCode(403);
	}

	@Test
	void test_printVorschauUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/vorschau/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_printVorschauForbidden() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/vorschau/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(403);
	}

	@Test
	void test_printArbeitsblattMitLoesungenUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/arbeitsblatt/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(401);
	}

	@Test
	void test_printKnobelkarteiUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/knobelkartei/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(401);
	}

}
