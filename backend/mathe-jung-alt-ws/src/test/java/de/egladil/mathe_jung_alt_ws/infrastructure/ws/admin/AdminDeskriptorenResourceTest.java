// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * OpenDeskriptorenResourceTest
 */
@QuarkusTest
public class AdminDeskriptorenResourceTest {

	@Test
	public void testLoadDeskriptorenEndpoint() {

		String expected = "[{\"id\":9,\"name\":\"A\",\"admin\":false},{\"id\":10,\"name\":\"B\",\"admin\":false},{\"id\":15,\"name\":\"Buch\",\"admin\":false},{\"id\":11,\"name\":\"C\",\"admin\":false},{\"id\":7,\"name\":\"EINS\",\"admin\":false},{\"id\":6,\"name\":\"IKID\",\"admin\":false},{\"id\":3,\"name\":\"Inklusion\",\"admin\":false},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false},{\"id\":14,\"name\":\"Logik\",\"admin\":false},{\"id\":1,\"name\":\"Mathematik\",\"admin\":false},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":false},{\"id\":18,\"name\":\"Nachbau\",\"admin\":true},{\"id\":17,\"name\":\"Person\",\"admin\":false},{\"id\":12,\"name\":\"Serie\",\"admin\":false},{\"id\":16,\"name\":\"Zeitschrift\",\"admin\":false},{\"id\":13,\"name\":\"Zitat\",\"admin\":false},{\"id\":8,\"name\":\"ZWEI\",\"admin\":false}]";

		given()
			.when().get("/admin/deskriptoren/v1")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

}
