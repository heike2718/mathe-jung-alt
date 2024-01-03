// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import de.egladil.mja_api.profiles.FullDatabaseAutorTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AutorAufgabensammlungenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AufgabensammlungenResource.class)
@TestProfile(FullDatabaseAutorTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AutorAufgabensammlungenResourceTest {

	@Test
	@Order(1)
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
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

		assertTrue(treffer.getItems().size() >= 6);
		assertTrue(treffer.getTrefferGesamt() >= 6);

		{

			Optional<AufgabensammlungSucheTrefferItem> optItem = treffer.getItems()
				.stream().filter(i -> "581cbd35-9423-414f-bda5-3eb2d05b979b".equals(i.getId())).findFirst();

			assertTrue(optItem.isPresent());
			AufgabensammlungSucheTrefferItem aufgabensammlungSucheTrefferItem = optItem.get();
			assertFalse(aufgabensammlungSucheTrefferItem.isFreigegeben());
			assertFalse(aufgabensammlungSucheTrefferItem.isPrivat());

		}
	}

	@Test
	@Order(3)
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void testLoadDetailsNichtFreigegeben() throws Exception {

		String id = "581cbd35-9423-414f-bda5-3eb2d05b979b";

		AufgabensammlungDetails treffer = given()
			.get(id + "/v1")
			.then()
			.statusCode(200)
			.extract()
			.as(AufgabensammlungDetails.class);

		List<Aufgabensammlungselement> elemente = treffer.getElemente();
		assertEquals(15, elemente.size());
		assertFalse(treffer.isFreigegeben());
		assertTrue(treffer.isSchreibgeschuetzt());
	}

	@Test
	@Order(5)
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void testLoadDetailsFreigegeben() throws Exception {

		String id = "13c62cfb-cfdd-41f1-b8a9-6c866e087718";

		AufgabensammlungDetails treffer = given()
			.get(id + "/v1")
			.then()
			.statusCode(200)
			.extract()
			.as(AufgabensammlungDetails.class);

		assertTrue(treffer.getElemente().size() >= 6);
		assertTrue(treffer.isFreigegeben());
		assertTrue(treffer.isSchreibgeschuetzt());
	}

}
