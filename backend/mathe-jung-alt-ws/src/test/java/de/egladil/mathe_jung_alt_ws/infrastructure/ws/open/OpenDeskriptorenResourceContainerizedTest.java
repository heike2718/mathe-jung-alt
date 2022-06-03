// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.open;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import de.egladil.mathe_jung_alt_ws.profiles.ContainerDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * OpenDeskriptorenResourceContainerizedTest
 */
@QuarkusTest
@TestHTTPEndpoint(OpenDeskriptorenResource.class)
@TestProfile(ContainerDatabaseTestProfile.class)
public class OpenDeskriptorenResourceContainerizedTest {

	@Test
	public void testLoadDeskriptorenEndpointWithKontextRAETSEL() {

		String expected = "[{\"id\":3,\"name\":\"Inklusion\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":14,\"name\":\"Logik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Zitat\",\"admin\":false,\"kontext\":\"RAETSEL\"}]";

		given()
			.when().get("?kontext=RAETSEL")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextNOOP() {

		String expected = "[]";
		given()
			.when().get("?kontext=NOOP")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextMEDIEN() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("?kontext=MEDIEN")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextQUELLEN() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("?kontext=QUELLEN")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextBILDER() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("?kontext=BILDER")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

}
