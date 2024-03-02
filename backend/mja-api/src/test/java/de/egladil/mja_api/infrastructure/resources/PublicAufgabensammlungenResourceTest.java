// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
import de.egladil.mja_api.profiles.FullDatabaseStandarduserTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * PublicAufgabensammlungenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AufgabensammlungenResource.class)
@TestProfile(FullDatabaseStandarduserTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class PublicAufgabensammlungenResourceTest {

	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	@Test
	@Order(1)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungenWithoutSuchparameters() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.get(
				"v1")
			.then()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(6, treffer.getItems().size());
		assertEquals(6l, treffer.getTrefferGesamt());

		{

			Optional<AufgabensammlungSucheTrefferItem> optItem = treffer.getItems()
				.stream().filter(i -> "581cbd35-9423-414f-bda5-3eb2d05b979b".equals(i.getId())).findFirst();

			assertTrue(optItem.isEmpty());

		}

		List<AufgabensammlungSucheTrefferItem> itemsFreigegeben = treffer.getItems()
			.stream().filter(i -> !"581cbd35-9423-414f-bda5-3eb2d05b979b".equals(i.getId())).toList();

		List<String> uuidsWithError = new ArrayList<>();

		for (AufgabensammlungSucheTrefferItem item : itemsFreigegeben) {

			if (!item.isFreigegeben() || item.isPrivat()) {

				uuidsWithError.add(item.getId());
			}

		}

		assertEquals(0, uuidsWithError.size());

	}

	@Test
	@Order(2)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungen_when_nur_referenz() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("referenz", "2021")
			.get(
				"v1")
			.then()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(0, treffer.getItems().size());
		assertEquals(0l, treffer.getTrefferGesamt());

	}

	@Test
	@Order(3)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungen_when_nur_name() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("name", "Mini")
			.get(
				"v1")
			.then()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(5, treffer.getItems().size());
		assertEquals(5l, treffer.getTrefferGesamt());

	}

	@Test
	@Order(4)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungen_when_nur_schwierigkeitsgrad() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("schwierigkeitsgrad", Schwierigkeitsgrad.EINS.toString())
			.get("v1")
			.then()
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(2, treffer.getItems().size());
		assertEquals(2l, treffer.getTrefferGesamt());
	}

	@Test
	@Order(5)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testFindAufgabensammlungen_when_nur_Referenztyp() throws Exception {

		AufgabensammlungSucheTreffer treffer = given()
			.queryParam("limit", "20")
			.queryParam("offset", "0")
			.queryParam("referenztyp", Referenztyp.MINIKAENGURU)
			.get(
				"v1")
			.then()
			.contentType(ContentType.JSON)
			.and()
			.extract()
			.as(AufgabensammlungSucheTreffer.class);

		assertEquals(5, treffer.getItems().size());
		assertEquals(5l, treffer.getTrefferGesamt());

	}

	@Test
	@Order(10)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testLoadDetails200() throws Exception {

		String id = "c09e5d63-9ec1-4884-a01e-08234db9cbf3";

		AufgabensammlungDetails treffer = given()
			.get(id + "/v1")
			.then()
			.statusCode(200)
			.extract()
			.as(AufgabensammlungDetails.class);

		List<Aufgabensammlungselement> elemente = treffer.getElemente();
		assertEquals(15, elemente.size());
		assertTrue(treffer.isSchreibgeschuetzt());
	}

	@Test
	@Order(11)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testLoadDetails403() throws Exception {

		String id = "581cbd35-9423-414f-bda5-3eb2d05b979b";

		given()
			.get(id + "/v1")
			.then()
			.statusCode(403);
	}

	@Test
	@Order(12)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_printVorschau_403() {

		given()
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.queryParam("font", "FIBEL_NORD")
			.queryParam("size", "LARGE")
			.accept(ContentType.JSON)
			.get(
				"c09e5d63-9ec1-4884-a01e-08234db9cbf3/vorschau/v1")
			.then()
			.statusCode(403);
	}

	@Test
	@Order(13)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_downloadLaTeX_403() {

		given()
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.queryParam("font", "FIBEL_NORD")
			.queryParam("size", "LARGE")
			.accept(APPLICATION_OCTET_STREAM)
			.get(
				"c09e5d63-9ec1-4884-a01e-08234db9cbf3/latex/v1")
			.then()
			.statusCode(403);
	}

	@Test
	@Order(14)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void test_printArbeitsblatt_when_QuelleWithInternet() {

		given()
			.queryParam("layoutAntwortvorschlaege", "BUCHSTABEN")
			.accept(ContentType.JSON)
			.get(
				"f2e390b1-0425-4095-b594-9ab91d2ac606/arbeitsblatt/v1")
			.then()
			.statusCode(200);

	}

}
