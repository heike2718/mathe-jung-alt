// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * RaetselgruppenResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselgruppenResource.class)
@TestProfile(FullDatabaseTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class RaetselgruppenResourceTest {

	@Test
	@Order(9)
	void testRaetselgruppeAnlegen() throws Exception {

		String requestBody = "{\"id\":\"neu\",\"name\":\"Minikänguruwettbewerb 2022 - Klasse 1\",\"kommentar\":null,\"schwierigkeitsgrad\":\"EINS\",\"referenztyp\":\"MINIKAENGURU\",\"referenz\":\"2022\",\"status\":\"ERFASST\"}";

		Response response = null;

		try {

			response = given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.post("");
		} catch (Exception e) {

			e.printStackTrace();
		}

		String responsePayload = response.asString();

		System.out.println("=> " + responsePayload);

		assertEquals(201, response.getStatusCode());

		RaetselgruppensucheTrefferItem raetselgruppe = new ObjectMapper().readValue(responsePayload,
			RaetselgruppensucheTrefferItem.class);
		System.out.println(raetselgruppe.getId());
		assertNotNull(raetselgruppe.getId());

	}

}
