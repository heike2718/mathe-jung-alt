// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.profiles.ContainerDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * AdminRaetselResourceContainerizedTest
 */
@QuarkusTest
@TestHTTPEndpoint(AdminRaetselResource.class)
@TestProfile(ContainerDatabaseTestProfile.class)
public class AdminRaetselResourceContainerizedTest {

	@Test
	void testZaehleRaetselMitDeskriptoren() {

		given()
			.when().get("size?deskriptoren=Minikänguru,A-1&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is("2"));
	}

	@Test
	void testFindRaetselMitDeskriptoren() {

		String expected = "[{\"id\":\"7a94e100-85e9-4ffb-903b-06835851063b\",\"schluessel\":\"02789\",\"name\":\"2022 zählen\",\"kommentar\":\"Minikänguru 2022 Klasse 1\",\"deskriptoren\":[{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"}]}]";

		given()
			.when().get("?deskriptoren=Minikänguru,A-1&typeDeskriptoren=STRING")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
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
					.post("");
			} catch (Exception e) {

				e.printStackTrace();
			}

			String responsePayload = response.asString();

			Raetsel raetsel = new ObjectMapper().readValue(responsePayload, Raetsel.class);
			System.out.println(raetsel.getId());
			assertNotNull(raetsel.getId());

			assertEquals(201, response.getStatusCode());

		}

	}

	@Test
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
					.put("");
			} catch (Exception e) {

				e.printStackTrace();
			}

			String responsePayload = response.asString();

			Raetsel raetsel = new ObjectMapper().readValue(responsePayload, Raetsel.class);
			assertEquals(5, raetsel.getAntwortvorschlaege().length);
			assertEquals("cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866", raetsel.getId());

			assertEquals(200, response.getStatusCode());

		}

	}

}
