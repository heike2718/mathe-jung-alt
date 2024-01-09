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
	void testFindAufgabensammlungen() throws Exception {

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

		assertEquals(5, treffer.getItems().size());
		assertEquals(5l, treffer.getTrefferGesamt());

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
	@Order(3)
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
	@Order(5)
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	void testLoadDetails403() throws Exception {

		String id = "581cbd35-9423-414f-bda5-3eb2d05b979b";

		given()
			.get(id + "/v1")
			.then()
			.statusCode(403);
	}

	@Test
	@Order(6)
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
	@Order(7)
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

}
