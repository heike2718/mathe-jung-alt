// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * RaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class RaetselResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(1)
	void testFindRaetselMitDeskriptorenUndSuchstring() throws Exception {

		Response response = given()
			.when().get("admin/v2?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING");
		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer suchergebnis = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(3, alleRaetsel.size());
		assertEquals(3, suchergebnis.getTrefferGesamt());

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(0);
			assertEquals("f2874374-ba5d-4641-a4ff-d5bb0346afd1", raetsel.getId());
			assertEquals("02774", raetsel.getSchluessel());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(2)
	void testFindRaetselMitDeskriptorenNumerischUndSuchstring() throws Exception {

		// Volltextsuche unterscheidet nicht zwischen zahlen und zählen!

		Response response = given()
			.when().get("admin/v2?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL");

		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer suchergebnis = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(6, alleRaetsel.size());
		assertEquals(6, suchergebnis.getTrefferGesamt());

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(0);
			assertEquals("4bfbb290-c808-4263-94f5-2fa19017cad1", raetsel.getId());
			assertEquals("01219", raetsel.getSchluessel());
		}

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(1);
			assertEquals("08dc5237-505d-4db2-b5f9-3fd3d74981e0", raetsel.getId());
			assertEquals("02602", raetsel.getSchluessel());
		}

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(2);
			assertEquals("a538e000-13a3-4f47-bd71-5d3cf48ad79e", raetsel.getId());
			assertEquals("02624", raetsel.getSchluessel());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(3)
	void testFindRaetselMitSuchstring() throws Exception {

		Response response = given()
			.when().get("admin/v2?suchstring=zählen&typeDeskriptoren=ORDINAL");
		response
			.then()
			.statusCode(200);

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer suchergebnis = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(17, alleRaetsel.size());
		assertEquals(17, suchergebnis.getTrefferGesamt());

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(0);
			assertEquals("4bfbb290-c808-4263-94f5-2fa19017cad1", raetsel.getId());
			assertEquals("01219", raetsel.getSchluessel());
		}

		{

			RaetselsucheTrefferItem raetsel = alleRaetsel.get(1);
			assertEquals("57d53a52-9609-46b2-bbfb-7e3d9e1983b5", raetsel.getId());
			assertEquals("02596", raetsel.getSchluessel());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(4)
	void testFindRaetselMitSuchstringKeinTreffer() {

		String expected = "{\"trefferGesamt\":0,\"treffer\":[]}";

		given()
			.when().get("admin/v2?suchstring=holleriedidudeldö&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(5)
	void testFindFromMinikaenguruWithCoordinates() throws Exception {

		Response response = given().when().get("admin/v2?deskriptoren=2,6,47,78&typeDeskriptoren=ORDINAL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer suchergebnis = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(1, alleRaetsel.size());
		assertEquals(1, suchergebnis.getTrefferGesamt());

		RaetselsucheTrefferItem treffer = alleRaetsel.get(0);
		assertEquals("02790", treffer.getSchluessel());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(6)
	void testFindWithSchluessel() throws Exception {

		Response response = given().when().get("admin/v2?suchstring=02790&typeDeskriptoren=ORDINAL");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		RaetselsucheTreffer suchergebnis = new ObjectMapper().readValue(responsePayload, RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(1, alleRaetsel.size());
		assertEquals(1, suchergebnis.getTrefferGesamt());

		RaetselsucheTrefferItem treffer = alleRaetsel.get(0);
		assertEquals("02790", treffer.getSchluessel());

	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR", "STANDARD" })
	@Order(7)
	void testRaetselDetailsLadenFound() throws Exception {

		Response response = given()
			.when().get("cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866/v1");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Raetsel treffer = new ObjectMapper().readValue(responsePayload, Raetsel.class);
		assertEquals("02622", treffer.getSchluessel());

		assertEquals(200, response.getStatusCode());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(8)
	void testRaetselDetailsLadenNotFound() throws Exception {

		given()
			.when().get("f4369b22/v1")
			.then()
			.statusCode(404)
			.body(is(""));
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(9)
	void testRaetselAnlegen() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsert.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.post("v1");
			} catch (Exception e) {

				e.printStackTrace();
			}

			String responsePayload = response.asString();

			System.out.println("=> " + responsePayload);

			assertEquals(201, response.getStatusCode());

			Raetsel raetsel = new ObjectMapper().readValue(responsePayload, Raetsel.class);
			System.out.println(raetsel.getId());
			assertNotNull(raetsel.getId());
		}

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(10)
	void testRaetselAendern() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.put("v1");
			} catch (Exception e) {

				e.printStackTrace();
			}

			String responsePayload = response.asString();

			assertEquals(200, response.getStatusCode());

			Raetsel raetsel = new ObjectMapper().readValue(responsePayload, Raetsel.class);
			assertEquals(5, raetsel.getAntwortvorschlaege().length);
			assertEquals("cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866", raetsel.getId());

			System.out.println("=> " + responsePayload);
		}

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(11)
	void testRaetselAendern404() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate404.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.put("v1");
			} catch (Exception e) {

				e.printStackTrace();
			}

			assertEquals(404, response.getStatusCode());
		}

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(12)
	void testRaetselAnlegen409() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsertUKViolation.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.post("v1");
			} catch (Exception e) {

				e.printStackTrace();
			}

			assertEquals(409, response.getStatusCode());

			String responsePayload = response.asString();
			MessagePayload messagePayload = new ObjectMapper().readValue(responsePayload, MessagePayload.class);
			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Der Schlüssel ist bereits vergeben.", messagePayload.getMessage());

		}

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(13)
	void testRaetselAendern409() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdateUKViolation.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.put("v1");
			} catch (Exception e) {

				e.printStackTrace();
			}

			assertEquals(409, response.getStatusCode());

			String responsePayload = response.asString();
			MessagePayload messagePayload = new ObjectMapper().readValue(responsePayload, MessagePayload.class);
			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Der Schlüssel ist bereits vergeben.", messagePayload.getMessage());
		}

	}

	@Test
	@TestSecurity(user = "standard", roles = { "STANDARD" })
	@Order(14)
	void testGeneratePDF() {

		given().accept(ContentType.JSON)
			.get("PDF/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=ANKREUZTABELLE").then().statusCode(200);

	}

	// @Test
	// @TestSecurity(user = "autor", roles = { "AUTOR" })
	// @Order(15)
	// void testGeneratePNGAutorAndOtherOwner() {
	//
	// given()
	// .header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
	// .cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
	// .contentType(ContentType.JSON)
	// .accept(ContentType.JSON)
	// .post("v1/PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d?layoutAntwortvorschlaege=ANKREUZTABELLE")
	// .then()
	// .statusCode(401);
	//
	// }

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(15)
	void testGeneratePNG() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.post("PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then()
			.statusCode(200);

	}

	@Test
	@Order(16)
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	void testLoadImages() {

		given().accept(ContentType.JSON).get("PNG/02610/v1").then().statusCode(200);
	}

	@Test
	@TestSecurity(user = "lehrer", roles = { "LEHRER" })
	@Order(17)
	void testGeneratePDFKeinTreffer() {

		given().get("PDF/2222222-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=BUCHSTABEN").then().statusCode(404);

	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(18)
	void testGeneratePNGKeinTreffer() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.post("PNG/2222222-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then().statusCode(404);
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(19)
	void testLoadImagesKeinTreffer() throws Exception {

		Response response = given().accept(ContentType.JSON).get("PNG/76767/v1");

		response.then().statusCode(200);

		String responsePayload = response.asString();
		Images dto = new ObjectMapper().readValue(responsePayload, Images.class);

		assertNull(dto.getImageFrage());
		assertNull(dto.getImageLoesung());
	}

}
