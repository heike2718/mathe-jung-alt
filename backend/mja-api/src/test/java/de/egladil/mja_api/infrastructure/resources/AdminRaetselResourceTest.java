// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.SuchmodusVolltext;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.raetsel.HerkunftRaetsel;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
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
import jakarta.persistence.EnumType;

/**
 * AdminRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AdminRaetselResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	private String suchstring = "Kinder%20Tiere%20pflanzen%20Kreis%20Monat%20Spielzeug%20Kekse%20Känguru";

	private String deskriptoren = "8,11";

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(3)
	void testFindRaetselAdminDeskriptorenAlsStringUndSuchstring() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "Minikänguru,A-1")
			.queryParam("suchstring", "zählen")
			.queryParam("modeFullTextSearch", SuchmodusVolltext.UNION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.STRING.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

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
	@Order(4)
	void testFindRaetselAdminKeinTreffer() {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "Minikänguru,A-1")
			.queryParam("suchstring", "holleriedidudeldö")
			.queryParam("modeFullTextSearch", SuchmodusVolltext.UNION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.STRING.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		assertEquals(0, suchergebnis.getTrefferGesamt());
		assertTrue(suchergebnis.getTreffer().isEmpty());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(5)
	void findRaetselAdmin_when_UNION_AND_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("suchstring", suchstring)
			.queryParam("modeFullTextSearch", SuchmodusVolltext.UNION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(8, alleRaetsel.size());
		assertEquals(8, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(5)
	void findRaetselAdmin_when_fallbackToDefaults() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("suchstring", suchstring)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(8, alleRaetsel.size());
		assertEquals(8, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(5)
	void findRaetselAdmin_when_deskriptorenUndSuchstringBlank() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(0, alleRaetsel.size());
		assertEquals(0, suchergebnis.getTrefferGesamt());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(6)
	void findRaetselAdmin_when_UNION_AND_NOT_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("suchstring", suchstring)
			.queryParam("modeFullTextSearch", SuchmodusVolltext.UNION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.NOT_LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.queryParam("limit", "20")
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(16, alleRaetsel.size());
		assertEquals(16, suchergebnis.getTrefferGesamt());

		assertEquals("02516", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(7)
	void findRaetselAdmin_when_INTERSECTION_AND_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "8")
			.queryParam("suchstring", "zahl%20känguru")
			.queryParam("modeFullTextSearch", SuchmodusVolltext.INTERSECTION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(3, alleRaetsel.size());
		assertEquals(3, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(8)
	void findRaetselAdmin_when_INTERSECTION_AND_NOT_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "6")
			.queryParam("suchstring", "zahl%20känguru")
			.queryParam("modeFullTextSearch", SuchmodusVolltext.INTERSECTION.toString())
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.NOT_LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(3, alleRaetsel.size());
		assertEquals(3, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(9)
	void testFindFromMinikaenguruWithCoordinates() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "2,6,47,78")
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE.toString())
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(1, alleRaetsel.size());
		assertEquals(1, suchergebnis.getTrefferGesamt());

		RaetselsucheTrefferItem treffer = alleRaetsel.get(0);
		assertEquals("02790", treffer.getSchluessel());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(10)
	void testFindWithSchluessel() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("suchstring", "02790")
			.queryParam("typeDeskriptoren", EnumType.ORDINAL.toString())
			.when()
			.get("admin/v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(1, alleRaetsel.size());
		assertEquals(1, suchergebnis.getTrefferGesamt());

		RaetselsucheTrefferItem treffer = alleRaetsel.get(0);
		assertEquals("02790", treffer.getSchluessel());

	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR", "STANDARD" })
	@Order(11)
	void testRaetselDetailsLadenFound() throws Exception {

		Raetsel treffer = given()
			.when().get("02606/v1").then()
			.statusCode(200)
			.and()
			.extract()
			.as(Raetsel.class);

		assertEquals("0ce69e0e-e1f8-4400-a2b9-61d3d6b0a82e", treffer.getId());
		assertEquals("02606", treffer.getSchluessel());
		assertTrue(treffer.isFreigegeben());

		HerkunftRaetsel herkunftRaetsel = treffer.getHerkunft();
		assertEquals("Heike Winkelvoß", herkunftRaetsel.getText());
		assertEquals(Quellenart.PERSON, herkunftRaetsel.getQuellenart());
		assertEquals(RaetselHerkunftTyp.EIGENKREATION, herkunftRaetsel.getHerkunftstyp());
		assertNull(herkunftRaetsel.getMediumUuid());
		assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", herkunftRaetsel.getId());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(12)
	void testRaetselDetailsLadenBadRequest() throws Exception {

		MessagePayload messagePayload = given()
			.when().get("cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866/v1")
			.then()
			.statusCode(400)
			.and()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("schluessel enthält ungültige Zeichen", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN", "STANDARD" })
	@Order(12)
	void testRaetselDetailsLadenNotFound() throws Exception {

		given()
			.when().get("76356/v1")
			.then()
			.statusCode(404);
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(13)
	void testRaetselAnlegen() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsert.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			try {

				Raetsel raetsel = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.post("v1")
					.then()
					.statusCode(201)
					.and()
					.contentType(ContentType.JSON)
					.extract()
					.as(Raetsel.class);

				assertNotNull(raetsel.getId());

				HerkunftRaetsel quelleUI = raetsel.getHerkunft();
				assertEquals("Heike Winkelvoß", quelleUI.getText());
				assertEquals(Quellenart.PERSON, quelleUI.getQuellenart());
				assertNull(quelleUI.getMediumUuid());
				assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", quelleUI.getId());

			} catch (Exception e) {

				fail(e.getMessage());
				e.printStackTrace();
			}

		}

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(14)
	void testRaetselAendern() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			try {

				Raetsel raetsel = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.put("v1")
					.then()
					.statusCode(200)
					.and()
					.contentType(ContentType.JSON)
					.extract()
					.as(Raetsel.class);

				assertEquals("cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866", raetsel.getId());

				HerkunftRaetsel quelleUI = raetsel.getHerkunft();
				assertEquals("Heike Winkelvoß", quelleUI.getText());
				assertEquals(Quellenart.PERSON, quelleUI.getQuellenart());
				assertNull(quelleUI.getMediumUuid());
				assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", quelleUI.getId());

			} catch (Exception e) {

				fail(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(15)
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
	@Order(16)
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
	@Order(17)
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
	@TestSecurity(user = "autor", roles = { "STANDARD" })
	@Order(18)
	void testGeneratePDF() {

		// Methode RaetselResource.raetselPDFGenerieren()

		given().accept(ContentType.JSON)
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get("PDF/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "STANDARD" })
	@Order(19)
	void testGeneratePDFLaTeXKaputt() {

		// Methode RaetselResource.raetselPDFGenerieren()

		given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get("PDF/69682087-be49-498f-b9ee-1636978aefc0/v1")
			.then()
			.statusCode(500);

	}

	@Test
	@TestSecurity(user = "autor", roles = { "STANDARD" })
	@Order(20)
	void testGeneratePDFGrafikFehlt() {

		// Methode RaetselResource.raetselPDFGenerieren()

		given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get("PDF/69959982-83f9-482d-a26c-8eb4a92bd6ff/v1")
			.then()
			.statusCode(500);

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(21)
	void testGeneratePNG() {

		given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.post("PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(Images.class);

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(22)
	void testGeneratePNGLaTeXInvalid() {

		given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.post("PNG/69682087-be49-498f-b9ee-1636978aefc0/v1")
			.then()
			.statusCode(500);

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(23)
	void testGeneratePNGGrafikFehlt() {

		given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.post("PNG/69959982-83f9-482d-a26c-8eb4a92bd6ff/v1")
			.then()
			.statusCode(500);

	}

	@Test
	@Order(24)
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	void testLoadImages() {

		given().accept(ContentType.JSON).get("PNG/02610/v1").then().statusCode(200);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(26)
	void testGeneratePNGKeinTreffer() {

		MessagePayload messagePayload = given()
			.queryParam("layoutAntwortvorschlaege", "ANKREUZTABELLE")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.post("PNG/00000000-0000-0000-0000-000000000000/v1")
			.then()
			.statusCode(404)
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Es gibt kein Raetsel mit dieser UUID", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(27)
	void testLoadImagesKeinTreffer() throws Exception {

		Images dto = given()
			.accept(ContentType.JSON)
			.get("PNG/76767/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(Images.class);

		assertNull(dto.getImageFrage());
		assertNull(dto.getImageLoesung());
	}

	@Test
	@TestSecurity(user = "lehrer", roles = { "LEHRER" })
	@Order(25)
	void testGeneratePDFKeinTreffer() {

		MessagePayload messagePayload = given()
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get("PDF/00000000-0000-0000-0000-000000000000/v1")
			.then()
			.statusCode(404)
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Es gibt kein Raetsel mit dieser UUID", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(26)
	void testDownloadLaTeXLogFiles() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.accept(ContentType.JSON)
			.get("latexlogs/02817/v1")
			.then()
			.statusCode(200);

	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(40)
	@DisplayName("when the URL embedded-images is called with an invalid raetselId, I expect statuscode 400")
	void downloadEmbeddedImages_400() throws Exception {

		MessagePayload messagePayload = given()
			.when()
			.get("embedded-images/554d4994/v1")
			.then()
			.statusCode(400)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("raetselId enthält ungültige Zeichen", messagePayload.getMessage());
	}

	@Test
	@Order(41)
	@DisplayName("when embedded-images is called without role, I expect statuscode 401")
	void downloadEmbeddedImages_401() throws Exception {

		given()
			.when()
			.get("embedded-images/554d4994-90b1-4baf-a7a0-cb5cb3b54ac6/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(42)
	@DisplayName("when there is no raetsel with the UUID and embedded-images is called, I expect statuscode 404")
	void downloadEmbeddedImages_404() throws Exception {

		MessagePayload messagePayload = given()
			.when()
			.get("embedded-images/8763142b-bded-4fe6-8eb9-243e9156f51c/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("Tja, dieses Rätsel gibt es leider nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(44)
	@DisplayName("when the URL raetsel-texte is called with an invalid raetselId, I expect statuscode 400")
	void downloadRaetselLaTeX_400() throws Exception {

		MessagePayload messagePayload = given()
			.when()
			.get("raetsel-texte/554d4994/v1")
			.then()
			.statusCode(400)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("raetselId enthält ungültige Zeichen", messagePayload.getMessage());
	}

	@Test
	@Order(45)
	@DisplayName("when raetsel-texte is called without role, I expect statuscode 401")
	void downloadRaetselLaTeX_401() throws Exception {

		given()
			.when()
			.get("raetsel-texte/554d4994-90b1-4baf-a7a0-cb5cb3b54ac6/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(46)
	@DisplayName("when there is no raetsel with the UUID and raetsel-texte is called, I expect statuscode 404")
	void downloadRaetselLaTeX_404() throws Exception {

		MessagePayload messagePayload = given()
			.when()
			.get("raetsel-texte/8763142b-bded-4fe6-8eb9-243e9156f51c/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("Tja, dieses Rätsel gibt es leider nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(50)
	void testRaetselMitAndererPersonAnlegen() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsertZitatPerson.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			try {

				Raetsel raetsel = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(requestBody)
					.post("v1")
					.then()
					.statusCode(201)
					.and()
					.contentType(ContentType.JSON)
					.extract()
					.as(Raetsel.class);

				assertNotNull(raetsel.getId());

				HerkunftRaetsel herkunft = raetsel.getHerkunft();
				assertEquals("David Hilbert", herkunft.getText());
				assertEquals(Quellenart.PERSON, herkunft.getQuellenart());
				assertNull(herkunft.getMediumUuid());
				assertEquals(RaetselHerkunftTyp.ZITAT, herkunft.getHerkunftstyp());
				assertFalse("8ef4d9b8-62a6-4643-8674-73ebaec52d98".equals(herkunft.getId()));

			} catch (Exception e) {

				fail(e.getMessage());
				e.printStackTrace();
			}

		}

	}
}
