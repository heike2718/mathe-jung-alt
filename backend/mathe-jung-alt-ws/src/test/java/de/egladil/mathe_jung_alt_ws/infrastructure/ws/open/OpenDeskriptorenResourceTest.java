// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.open;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * OpenDeskriptorenResourceTest
 */
@QuarkusTest
public class OpenDeskriptorenResourceTest {

	@Test
	public void testLoadDeskriptorenEndpointWithKontextRAETSEL() {

		String expected = "[{\"id\":17,\"name\":\"ab Klasse 9\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":22,\"name\":\"Algebra\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":25,\"name\":\"Analysis\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":30,\"name\":\"Frühling\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":24,\"name\":\"Geometrie\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":41,\"name\":\"Grundschule\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":32,\"name\":\"Herbst\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":3,\"name\":\"Inklusion\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":35,\"name\":\"Jahreswechsel\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Klassen 1/2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":14,\"name\":\"Klassen 3/4\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":15,\"name\":\"Klassen 5/6\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":16,\"name\":\"Klassen 7/8\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":26,\"name\":\"Kombinatorik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":21,\"name\":\"Logik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":36,\"name\":\"Ostern\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":42,\"name\":\"Sekundarstufe 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":43,\"name\":\"Sekundarstufe 2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":31,\"name\":\"Sommer\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":28,\"name\":\"Wahrscheinlichkeit\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":34,\"name\":\"Weihnachten\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":33,\"name\":\"Winter\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":27,\"name\":\"Zahlentheorie\",\"admin\":false,\"kontext\":\"RAETSEL\"}]";

		given()
			.when().get("/deskriptoren/v1?kontext=RAETSEL")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextNOOP() {

		String expected = "[]";
		given()
			.when().get("/deskriptoren/v1?kontext=NOOP")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextMEDIEN() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("/deskriptoren/v1?kontext=MEDIEN")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextQUELLEN() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("/deskriptoren/v1?kontext=QUELLEN")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

	@Test
	public void testLoadDeskriptorenEndpointWithKontextBILDER() {

		String expected = "{\"level\":\"ERROR\",\"message\":\"Unerlaubter kontext: nur [NOOP, RAETSEL] erlaubt.\"}";
		given()
			.when().get("/deskriptoren/v1?kontext=BILDER")
			.then()
			.statusCode(400)
			.body(is(expected));
	}

}
