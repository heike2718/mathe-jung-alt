// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.aufgabensammlungen.Aufgabensammlungselement;
import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungDetails;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTreffer;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTrefferItem;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungPayload;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungselementPayload;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
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
@TestMethodOrder(OrderAnnotation.class)
public class AufgabensammlungenResourceTest {

	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	@Order(1)
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void testFindAufgabensammlungen() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("referenz", "2022")
			.queryParam("referenztyp", "MINIKAENGURU")
			.queryParam("schwierigkeitsgrad", "EINS")
			.queryParam("sortAttribute", "name")
			.queryParam("sortDirection", "asc")
			.get(
				"v1")
			.then()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(1, treffer.getItems().size());
		assertEquals(1l, treffer.getTrefferGesamt());

		assertEquals("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc", treffer.getItems().get(0).getId());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(2)
	void testFindAufgabensammlungenKeinTreffer() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("referenz", "2022")
			.queryParam("referenztyp", "SERIE")
			.queryParam("schwierigkeitsgrad", "EINS")
			.queryParam("sortAttribute", "name")
			.queryParam("sortDirection", "asc")
			.get(
				"v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(0, treffer.getItems().size());
		assertEquals(0l, treffer.getTrefferGesamt());

	}

	@Test
	@Order(3)
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void testLoadDetails() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		AufgabensammlungDetails treffer = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.get(id + "/v1")
			.then()
			.statusCode(200)
			.extract()
			.as(AufgabensammlungDetails.class);

		List<Aufgabensammlungselement> elemente = treffer.getElemente();
		assertEquals(6, elemente.size());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(4)
	void testLoadDetailsKeinTreffer() throws Exception {

		String id = "07c62cfb-cfdd-41f1-b8a9-6c866e087718";

		MessagePayload responsePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.get(id + "/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertTrue(responsePayload.getMessage().contains("kein Treffer"));

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(5)
	void testAufgabensammlungeAnlegenOhneReferenz() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setFreigegeben(false);

		AufgabensammlungSucheTrefferItem responsePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(201)
			.and()
			.extract()
			.as(AufgabensammlungSucheTrefferItem.class);

		System.out.println("=> " + responsePayload.toString());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(6)
	void testAufgabensammlungeAnlegenOhneReferenzGleicherNameAnderesLevel() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.VORSCHULE);
		payload.setFreigegeben(false);

		MessagePayload responsePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(409)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		System.out.println("=> " + responsePayload.toString());

		assertTrue(responsePayload.getMessage().contains("Es gibt bereits eine Aufgabensammlung mit diesem Namen."));

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(7)
	void testAufgabensammlungeAnlegenUndAendern() throws Exception {

		AufgabensammlungSucheTrefferItem trefferitem = null;
		String expectedKommentar = "Kommentar aus dem Test";

		{

			EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
			payload.setId("neu");
			payload.setName("Minikänguru 2005 - Klasse 2");
			payload.setReferenz("2005");
			payload.setReferenztyp(Referenztyp.MINIKAENGURU);
			payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
			payload.setFreigegeben(false);

			trefferitem = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.post("v1")
				.then()
				.statusCode(201)
				.extract()
				.as(AufgabensammlungSucheTrefferItem.class);

			assertNotNull(trefferitem.getId());
		}

		{

			AufgabensammlungSucheTreffer treffer = given()
				.queryParam("limit", "20")
				.queryParam("offset", "0")
				.queryParam("referenz", "2005")
				.queryParam("referenztyp", "MINIKAENGURU")
				.queryParam("schwierigkeitsgrad", "ZWEI")
				.queryParam("sortAttribute", "name")
				.queryParam("sortDirection", "asc")
				.contentType(ContentType.JSON)
				.get(
					"v1")
				.then()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(AufgabensammlungSucheTreffer.class);

			trefferitem = treffer.getItems().get(0);

		}

		if (trefferitem != null) {

			EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
			payload.setKommentar(expectedKommentar);
			payload.setId(trefferitem.getId());
			payload.setName(trefferitem.getName());
			payload.setReferenz(trefferitem.getReferenz());
			payload.setReferenztyp(trefferitem.getReferenztyp());
			payload.setSchwierigkeitsgrad(trefferitem.getSchwierigkeitsgrad());
			payload.setFreigegeben(trefferitem.isFreigegeben());
			payload.setPrivat(trefferitem.isPrivat());

			AufgabensammlungSucheTrefferItem theItem = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.put("v1")
				.then()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(AufgabensammlungSucheTrefferItem.class);

			System.out.println(theItem.getId());
			assertEquals(trefferitem.getId(), theItem.getId());

		}

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	@Order(8)
	void testAufgabensammlungeAnlegenGleicherName() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Minikänguru 2005 - Klasse 2");
		payload.setReferenz("2003");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(409)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Es gibt bereits eine Aufgabensammlung mit diesem Namen.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(9)
	void testAufgabensammlungeAnlegenGleicheReferenz() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("neu");
		payload.setName("Aufgabensammlung XY");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(409)
			.extract()
			.as(MessagePayload.class);

		// System.out.println(messagePayload.toString());

		assertEquals("Es gibt bereits eine Aufgabensammlung mit der gleichen Referenz.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(10)
	void testAufgabensammlungeAnlegenIdNichtNeu() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("20582897-68be-41dd-ac28-63cc80b07f85");
		payload.setName("Aufgabensammlung XY");
		payload.setReferenz("2010");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(400)
			.extract()
			.as(MessagePayload.class);

		assertEquals("POST darf nur mit id='neu' aufgerufen werden", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(11)
	void testAufgabensammlungeAendernReferenzdublette() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("13c62cfb-cfdd-41f1-b8a9-6c866e087718");
		payload.setName("Aufgabensammlung XY");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("v1")
			.then()
			.statusCode(409)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Es gibt bereits eine Aufgabensammlung mit der gleichen Referenz.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(12)
	void testAufgabensammlungeAendernNamendublette() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc");
		payload.setName("Minikänguru 2022 - Inklusion");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("v1")
			.then()
			.statusCode(409)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Es gibt bereits eine Aufgabensammlung mit diesem Namen.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(13)
	void testAufgabensammlungeAendernUnbekannt() throws Exception {

		EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
		payload.setId("00000000-0000-0000-0000-000000000000");
		payload.setName("Minikänguru 2009 - Klasse 2");
		payload.setReferenz("2009");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
		payload.setFreigegeben(false);

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("v1")
			.then()
			.statusCode(404)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Diese Aufgabensammlung gibt es nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(20)
	void elementAnlegenAendernLoeschen() throws Exception {

		String aufgabensammlungUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";
		String elementUuid = null;

		{

			System.out.println("Details laden");

			AufgabensammlungDetails treffer = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.get(aufgabensammlungUuid + "/v1")
				.then()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(AufgabensammlungDetails.class);

			List<Aufgabensammlungselement> elemente = treffer.getElemente();
			assertEquals(12, elemente.size());
		}

		{

			System.out.println("Element anlegen");

			EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
			payload.setId("neu");
			payload.setNummer("A-5");
			payload.setPunkte(300);
			payload.setRaetselSchluessel("02618");

			AufgabensammlungDetails aufgabensammlung = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.post(aufgabensammlungUuid + "/elemente/v1")
				.then()
				.contentType(ContentType.JSON)
				.and()
				.statusCode(200)
				.extract()
				.as(AufgabensammlungDetails.class);

			System.out.println(aufgabensammlung.getId());

			List<Aufgabensammlungselement> elemente = aufgabensammlung.getElemente();

			assertEquals(13, elemente.size());

			Optional<Aufgabensammlungselement> optNeu = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
				.findFirst();
			elementUuid = optNeu.get().getId();
		}

		{

			System.out.println("Element ändern");

			if (elementUuid != null) {

				EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
				payload.setId(elementUuid);
				payload.setNummer("B-5");
				payload.setPunkte(300);
				payload.setRaetselSchluessel("02618");

				AufgabensammlungDetails aufgabensammlung = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(payload)
					.put(aufgabensammlungUuid + "/elemente/v1")
					.then()
					.contentType(ContentType.JSON)
					.and()
					.statusCode(200)
					.extract()
					.as(AufgabensammlungDetails.class);

				System.out.println(aufgabensammlung.getId());

				List<Aufgabensammlungselement> elemente = aufgabensammlung.getElemente();

				assertEquals(13, elemente.size());
				Optional<Aufgabensammlungselement> opt = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
					.findFirst();

				assertEquals("B-5", opt.get().getNummer());

			}
		}

		{

			System.out.println("Element löschen");

			if (elementUuid != null) {

				AufgabensammlungDetails aufgabensammlung = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.delete(aufgabensammlungUuid + "/elemente/" + elementUuid + "/v1")
					.then()
					.statusCode(200)
					.and()
					.contentType(ContentType.JSON)
					.extract()
					.as(AufgabensammlungDetails.class);

				System.out.println(aufgabensammlung.getId());

				List<Aufgabensammlungselement> elemente = aufgabensammlung.getElemente();

				assertEquals(12, elemente.size());

				Optional<Aufgabensammlungselement> opt = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
					.findFirst();
				assertTrue(opt.isEmpty());
			}
		}
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR", "STANDARD" })
	@Order(21)
	void elementLoeschenGruppeExistiertNicht() throws Exception {

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete("00000000-0000-0000-0000-000000000000/elemente/00000000-0000-0000-0000-000000000000/v1")
			.then()
			.statusCode(404)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Ups, da ist aber etwas komplett schiefgelaufen", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(22)
	void elementLoeschenElementExistiertNicht() throws Exception {

		AufgabensammlungDetails treffer = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/00000000-0000-0000-0000-000000000000/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(AufgabensammlungDetails.class);

		List<Aufgabensammlungselement> elemente = treffer.getElemente();
		assertEquals(6, elemente.size());

		Aufgabensammlungselement element = elemente.get(0);
		assertEquals("02774", element.getRaetselSchluessel());
		assertEquals(300, element.getPunkte());
		assertEquals("A-1", element.getNummer());
		assertEquals("Tierbeine zählen", element.getName());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR", "STANDARD" })
	@Order(23)
	void elementAnlegenGruppeExistiertNicht() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("neu");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("00000000-0000-0000-0000-000000000000/elemente/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Tja, diese Aufgabensammlung gibt es gar nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(24)
	void elementAnlegenRaetselExistiertNicht() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("neu");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("77777");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc/elemente/v1").then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Tja, mit dem gewünschen Schlüssel gibt es gar kein Rätsel.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(24)
	void elementAnlegenNummerSchonEnthalten() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("neu");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02621");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/v1")
			.then()
			.statusCode(409)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("In dieser Aufgabensammlung gibt es bereits ein Element mit der gewählten Nummer",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(25)
	void elementAnlegenRaetselSchonEnthalten() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("neu");
		payload.setNummer("A-5");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02774");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/v1")
			.then()
			.statusCode(409)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Das Rätsel gibt es in dieser Aufgabensammlung schon.",
			messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(26)
	void elementAendernGruppeExistiertNicht() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("bff1c941-1774-4489-bb3b-484361796cd2");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("00000000-0000-0000-0000-000000000000/elemente/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Tja, diese Aufgabensammlung gibt es gar nicht.",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(27)
	void elementAendernElementExistiertNicht() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("00000000-0000-0000-0000-000000000000");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc/elemente/v1")
			.then()
			.statusCode(404)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Tja, dieses Element gibt es gar nicht.",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(28)
	void elementAendernKonflikt() throws Exception {

		EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
		payload.setId("bff1c941-1774-4489-bb3b-484361796cd2");
		payload.setNummer("A-1");
		payload.setPunkte(300);
		payload.setRaetselSchluessel("02789");

		MessagePayload messagePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.put("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc/elemente/v1")
			.then()
			.statusCode(409)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("Konflikt", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(28)
	void elementAendernNummerExistiert() throws Exception {

		String elementUuid = null;

		{

			System.out.println("Element anlegen");

			EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
			payload.setId("neu");
			payload.setNummer("B-3");
			payload.setPunkte(400);
			payload.setRaetselSchluessel("02629");

			AufgabensammlungDetails aufgabensammlung = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.post("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/v1")
				.then()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(AufgabensammlungDetails.class);

			List<Aufgabensammlungselement> elemente = aufgabensammlung.getElemente();
			elementUuid = elemente.stream().filter(el -> "B-3".equals(el.getNummer())).findFirst().get().getId();
		}

		{

			System.out.println("Element ändern");

			EditAufgabensammlungselementPayload payload = new EditAufgabensammlungselementPayload();
			payload.setId(elementUuid);
			payload.setNummer("B-2");
			payload.setPunkte(300);
			payload.setRaetselSchluessel("02629");

			MessagePayload messagePayload = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.put("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/v1")
				.then()
				.statusCode(409)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(MessagePayload.class);

			assertEquals("In dieser Aufgabensammlung gibt es bereits ein Element mit der gewählten Nummer",
				messagePayload.getMessage());

		}

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	@Order(28)
	void test_downloadLaTeX() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.accept(APPLICATION_OCTET_STREAM)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/latex/v1")
			.then().statusCode(200);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(29)
	void test_downloadLaTeXKeinTreffer() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.accept(APPLICATION_OCTET_STREAM)
			.get(
				"33333333-71c5-423e-b355-bf90b5bb344d/latex/v1")
			.then()
			.statusCode(404);
	}

	@Test
	@Order(30)
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void test_printVorschau() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.queryParam("font", "FIBEL_NORD")
			.queryParam("size", "LARGE")
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/vorschau/v1")
			.then()
			.statusCode(200);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	@Order(31)
	void test_printVorschauKeinTreffer() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"33333333-71c5-423e-b355-bf90b5bb344d/vorschau/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then()
			.statusCode(404);
	}

	@Test
	@Order(32)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_printArbeitsblattMitLoesungen() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.queryParam("font", "DRUCK_BY_WOK")
			.queryParam("size", "LARGE")
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/arbeitsblatt/v1")
			.then()
			.statusCode(200);
	}

	@Test
	@Order(33)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_printKnobelkartei() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.queryParam("font", "FIBEL_SUED")
			.queryParam("size", "LARGE")
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/knobelkartei/v1")
			.then()
			.statusCode(200);
	}

}
