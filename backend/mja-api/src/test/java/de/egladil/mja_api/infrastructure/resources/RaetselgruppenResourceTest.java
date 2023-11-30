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

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
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
@TestMethodOrder(OrderAnnotation.class)
public class RaetselgruppenResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	@Order(1)
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void testFindRaetselgruppen() throws Exception {

		RaetselgruppensucheTreffer treffer = given()
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
			.as(RaetselgruppensucheTreffer.class);

		assertEquals(1, treffer.getItems().size());
		assertEquals(1l, treffer.getTrefferGesamt());

		assertEquals("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc", treffer.getItems().get(0).getId());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(2)
	void testFindRaetselgruppenKeinTreffer() throws Exception {

		RaetselgruppensucheTreffer treffer = given()
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
			.as(RaetselgruppensucheTreffer.class);

		assertEquals(0, treffer.getItems().size());
		assertEquals(0l, treffer.getTrefferGesamt());

	}

	@Test
	@Order(3)
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void testLoadDetails() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		RaetselgruppeDetails treffer = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.get(id + "/v1")
			.then()
			.statusCode(200)
			.extract()
			.as(RaetselgruppeDetails.class);

		List<Raetselgruppenelement> elemente = treffer.getElemente();
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
	void testRaetselgruppeAnlegenOhneReferenz() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.GRUNDSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

		RaetselgruppensucheTrefferItem responsePayload = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payload)
			.post("v1")
			.then()
			.statusCode(201)
			.and()
			.extract()
			.as(RaetselgruppensucheTrefferItem.class);

		System.out.println("=> " + responsePayload.toString());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(6)
	void testRaetselgruppeAnlegenOhneReferenzGleicherNameAnderesLevel() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Kandidaten Minikänguru");
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.VORSCHULE);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertTrue(responsePayload.getMessage().contains("Es gibt bereits eine Rätselgruppe mit diesem Namen."));

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(7)
	void testRaetselgruppeAnlegenUndAendern() throws Exception {

		RaetselgruppensucheTrefferItem raetselgruppensucheTrefferItem = null;
		String expectedKommentar = "Kommentar aus dem Test";

		{

			EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
			payload.setId("neu");
			payload.setName("Minikänguru 2005 - Klasse 2");
			payload.setReferenz("2005");
			payload.setReferenztyp(Referenztyp.MINIKAENGURU);
			payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
			payload.setStatus(DomainEntityStatus.ERFASST);

			raetselgruppensucheTrefferItem = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.post("v1")
				.then()
				.statusCode(201)
				.extract()
				.as(RaetselgruppensucheTrefferItem.class);

			assertNotNull(raetselgruppensucheTrefferItem.getId());
		}

		{

			RaetselgruppensucheTreffer treffer = given()
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
				.as(RaetselgruppensucheTreffer.class);

			raetselgruppensucheTrefferItem = treffer.getItems().get(0);

		}

		if (raetselgruppensucheTrefferItem != null) {

			EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
			payload.setKommentar(expectedKommentar);
			payload.setId(raetselgruppensucheTrefferItem.getId());
			payload.setName(raetselgruppensucheTrefferItem.getName());
			payload.setReferenz(raetselgruppensucheTrefferItem.getReferenz());
			payload.setReferenztyp(raetselgruppensucheTrefferItem.getReferenztyp());
			payload.setSchwierigkeitsgrad(raetselgruppensucheTrefferItem.getSchwierigkeitsgrad());
			payload.setStatus(raetselgruppensucheTrefferItem.getStatus());

			RaetselgruppensucheTrefferItem theItem = given()
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
				.as(RaetselgruppensucheTrefferItem.class);

			System.out.println(theItem.getId());
			assertEquals(raetselgruppensucheTrefferItem.getId(), theItem.getId());

		}

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	@Order(8)
	void testRaetselgruppeAnlegenGleicherName() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Minikänguru 2005 - Klasse 2");
		payload.setReferenz("2003");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertEquals("Es gibt bereits eine Rätselgruppe mit diesem Namen.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(9)
	void testRaetselgruppeAnlegenGleicheReferenz() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("neu");
		payload.setName("Rätselgruppe XY");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertEquals("Es gibt bereits eine Rätselgruppe mit der gleichen Referenz.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(10)
	void testRaetselgruppeAnlegenIdNichtNeu() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("012345-abcde");
		payload.setName("Rätselgruppe XY");
		payload.setReferenz("2010");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setStatus(DomainEntityStatus.ERFASST);

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
	void testRaetselgruppeAendernReferenzdublette() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("13c62cfb-cfdd-41f1-b8a9-6c866e087718");
		payload.setName("Rätselgruppe XY");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertEquals("Es gibt bereits eine Rätselgruppe mit der gleichen Referenz.", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(12)
	void testRaetselgruppeAendernNamendublette() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc");
		payload.setName("Minikänguru 2022 - Inklusion");
		payload.setReferenz("2022");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertEquals("Es gibt bereits eine Rätselgruppe mit diesem Namen.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(13)
	void testRaetselgruppeAendernUnbekannt() throws Exception {

		EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
		payload.setId("00000000-0000-0000-0000-000000000000");
		payload.setName("Minikänguru 2009 - Klasse 2");
		payload.setReferenz("2009");
		payload.setReferenztyp(Referenztyp.MINIKAENGURU);
		payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.ZWEI);
		payload.setStatus(DomainEntityStatus.ERFASST);

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

		assertEquals("Diese Rätselgruppe gibt es nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(20)
	void raetselgruppenelementAnlegenAendernLoeschen() throws Exception {

		String raetselgruppeUuid = "0af9f6e3-9e25-41a1-887d-0c9e6e9f57dc";
		String elementUuid = null;

		{

			System.out.println("Details laden");

			RaetselgruppeDetails treffer = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.get(raetselgruppeUuid + "/v1")
				.then()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.extract()
				.as(RaetselgruppeDetails.class);

			List<Raetselgruppenelement> elemente = treffer.getElemente();
			assertEquals(12, elemente.size());
		}

		{

			System.out.println("Element anlegen");

			EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
			payload.setId("neu");
			payload.setNummer("A-5");
			payload.setPunkte(300);
			payload.setRaetselSchluessel("02618");

			RaetselgruppeDetails raetselgruppe = given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(payload)
				.post(raetselgruppeUuid + "/elemente/v1")
				.then()
				.contentType(ContentType.JSON)
				.and()
				.statusCode(200)
				.extract()
				.as(RaetselgruppeDetails.class);

			System.out.println(raetselgruppe.getId());

			List<Raetselgruppenelement> elemente = raetselgruppe.getElemente();

			assertEquals(13, elemente.size());

			Optional<Raetselgruppenelement> optNeu = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
				.findFirst();
			elementUuid = optNeu.get().getId();
		}

		{

			System.out.println("Element ändern");

			if (elementUuid != null) {

				EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
				payload.setId(elementUuid);
				payload.setNummer("B-5");
				payload.setPunkte(300);
				payload.setRaetselSchluessel("02618");

				RaetselgruppeDetails raetselgruppe = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.body(payload)
					.put(raetselgruppeUuid + "/elemente/v1")
					.then()
					.contentType(ContentType.JSON)
					.and()
					.statusCode(200)
					.extract()
					.as(RaetselgruppeDetails.class);

				System.out.println(raetselgruppe.getId());

				List<Raetselgruppenelement> elemente = raetselgruppe.getElemente();

				assertEquals(13, elemente.size());
				Optional<Raetselgruppenelement> opt = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
					.findFirst();

				assertEquals("B-5", opt.get().getNummer());

			}
		}

		{

			System.out.println("Element löschen");

			if (elementUuid != null) {

				RaetselgruppeDetails raetselgruppe = given()
					.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
					.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
					.contentType(ContentType.JSON)
					.delete(raetselgruppeUuid + "/elemente/" + elementUuid + "/v1")
					.then()
					.statusCode(200)
					.and()
					.contentType(ContentType.JSON)
					.extract()
					.as(RaetselgruppeDetails.class);

				System.out.println(raetselgruppe.getId());

				List<Raetselgruppenelement> elemente = raetselgruppe.getElemente();

				assertEquals(12, elemente.size());

				Optional<Raetselgruppenelement> opt = elemente.stream().filter(el -> "02618".equals(el.getRaetselSchluessel()))
					.findFirst();
				assertTrue(opt.isEmpty());
			}
		}
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR", "STANDARD" })
	@Order(21)
	void raetselgruppenelementLoeschenGruppeExistiertNicht() throws Exception {

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
	void raetselgruppenelementLoeschenElementExistiertNicht() throws Exception {

		RaetselgruppeDetails treffer = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.delete("13c62cfb-cfdd-41f1-b8a9-6c866e087718/elemente/00000000-0000-0000-0000-000000000000/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(RaetselgruppeDetails.class);

		List<Raetselgruppenelement> elemente = treffer.getElemente();
		assertEquals(6, elemente.size());

		Raetselgruppenelement element = elemente.get(0);
		assertEquals("02774", element.getRaetselSchluessel());
		assertEquals(300, element.getPunkte());
		assertEquals("A-1", element.getNummer());
		assertEquals("Tierbeine zählen", element.getName());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR", "STANDARD" })
	@Order(23)
	void elementAnlegenGruppeExistiertNicht() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("Tja, diese Rätselgruppe gibt es gar nicht.", messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(24)
	void elementAnlegenRaetselExistiertNicht() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(25)
	void elementAnlegenRaetselSchonEnthalten() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("Das Rätsel gibt es in dieser Rätselgruppe schon.",
			messagePayload.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(26)
	void elementAendernGruppeExistiertNicht() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("Tja, diese Rätselgruppe gibt es gar nicht.",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(27)
	void elementAendernElementExistiertNicht() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("Tja, dieses Rätselgruppenelement gibt es gar nicht.",
			messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(28)
	void elementAendernRaetselgruppenkonflikt() throws Exception {

		EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

		assertEquals("Rätselgruppenkonflikt", messagePayload.getMessage());

	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN", "STANDARD" })
	@Order(28)
	void elementAendernNummerExistiert() throws Exception {

		String elementUuid = null;

		{

			System.out.println("Element anlegen");

			EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
			payload.setId("neu");
			payload.setNummer("B-3");
			payload.setPunkte(400);
			payload.setRaetselSchluessel("02629");

			RaetselgruppeDetails raetselgruppe = given()
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
				.as(RaetselgruppeDetails.class);

			List<Raetselgruppenelement> elemente = raetselgruppe.getElemente();
			elementUuid = elemente.stream().filter(el -> "B-3".equals(el.getNummer())).findFirst().get().getId();
		}

		{

			System.out.println("Element ändern");

			EditRaetselgruppenelementPayload payload = new EditRaetselgruppenelementPayload();
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

			assertEquals("In dieser Rätselgruppe gibt es bereits ein Element mit der gewählten Nummer",
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
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/latex/v1?layoutAntwortvorschlaege=BUCHSTABEN")
			.then().statusCode(200);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	@Order(29)
	void test_downloadLaTeXKeinTreffer() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"33333333-71c5-423e-b355-bf90b5bb344d/latex/v1?layoutAntwortvorschlaege=BUCHSTABEN")
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
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/vorschau/v1?layoutAntwortvorschlaege=BUCHSTABEN&font=FIBEL_NORD&size=LARGE")
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
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/arbeitsblatt/v1?font=FIBEL_NORD&size=LARGE")
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
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.get(
				"10257506-71c5-423e-b355-bf90b5bb344d/knobelkartei/v1?font=FIBEL_SUED&size=HUGE")
			.then()
			.statusCode(200);
	}

}
