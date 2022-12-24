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

import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import de.egladil.web.mja_auth.dto.MessagePayload;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
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

	@Test
	@Order(1)
	void testFindRaetselMitDeskriptorenUndSuchstring() throws Exception {

		Response response = given()
			.when().get("v1?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING");
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
	@Order(2)
	void testFindRaetselMitDeskriptorenNumerischUndSuchstring() throws Exception {

		// Volltextsuche unterscheidet nicht zwischen zahlen und zählen!

		Response response = given()
			.when().get("v1?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL");

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
	@Order(3)
	void testFindRaetselMitSuchstring() throws Exception {

		Response response = given()
			.when().get("v1?suchstring=zählen&typeDeskriptoren=ORDINAL");
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
	@Order(4)
	void testFindRaetselMitSuchstringKeinTreffer() {

		String expected = "{\"trefferGesamt\":0,\"treffer\":[]}";

		given()
			.when().get("v1?suchstring=holleriedidudeldö&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	@Order(5)
	void testFindFromMinikaenguruWithCoordinates() throws Exception {

		Response response = given().when().get("v1?deskriptoren=2,6,47,78&typeDeskriptoren=ORDINAL");

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
	@Order(6)
	void testFindWithSchluessel() throws Exception {

		Response response = given().when().get("v1?suchstring=02790&typeDeskriptoren=ORDINAL");

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
	@Order(7)
	void testRaetselDetailsLadenFound() throws Exception {

		Response response = given()
			.when().get("v1/cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866");

		String responsePayload = response.asString();
		System.out.println(responsePayload);

		Raetsel treffer = new ObjectMapper().readValue(responsePayload, Raetsel.class);
		assertEquals("02622", treffer.getSchluessel());

		assertEquals(200, response.getStatusCode());

	}

	@Test
	@Order(8)
	void testRaetselDetailsLadenNotFound() throws Exception {

		given()
			.when().get("v1/f4369b22")
			.then()
			.statusCode(404)
			.body(is(""));
	}

	@Test
	@Order(9)
	void testRaetselAnlegen() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsert.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
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
	@Order(10)
	void testRaetselAendern() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
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
	@Order(11)
	void testRaetselAendern404() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate404.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
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
	@Order(12)
	void testRaetselAnlegen409() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsertUKViolation.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
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
	@Order(13)
	void testRaetselAendern409() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdateUKViolation.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			Response response = null;

			try {

				response = given()
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
	@Order(14)
	void testGeneratePDF() {

		given().accept(ContentType.JSON)
			.get("v1/PDF/a4c4d45e-4a81-4bde-a6a3-54464801716d?layoutAntwortvorschlaege=ANKREUZTABELLE").then().statusCode(200);

	}

	@Test
	@Order(15)
	void testGeneratePNG() {

		given().contentType(ContentType.JSON).accept(ContentType.JSON)
			.post("v1/PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then().statusCode(200);

	}

	@Test
	@Order(16)
	void testLoadImages() {

		given().accept(ContentType.JSON).get("v1/PNG/02610").then().statusCode(200);
	}

	@Test
	@Order(17)
	void testGeneratePDFKeinTreffer() {

		given().get("v1/PDF/2222222-4a81-4bde-a6a3-54464801716d?layoutAntwortvorschlaege=BUCHSTABEN").then().statusCode(404);

	}

	@Test
	@Order(18)
	void testGeneratePNGKeinTreffer() {

		given().contentType(ContentType.JSON).accept(ContentType.JSON)
			.post("v1/PNG/2222222-4a81-4bde-a6a3-54464801716d?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then().statusCode(404);

	}

	@Test
	@Order(19)
	void testLoadImagesKeinTreffer() throws Exception {

		Response response = given().accept(ContentType.JSON).get("v1/PNG/76767");

		response.then().statusCode(200);

		String responsePayload = response.asString();
		Images dto = new ObjectMapper().readValue(responsePayload, Images.class);

		assertNull(dto.getImageFrage());
		assertNull(dto.getImageLoesung());
	}

}
