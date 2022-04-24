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

		String expected = "[{\"id\":9,\"name\":\"A\",\"adminOnly\":false},{\"id\":10,\"name\":\"B\",\"adminOnly\":false},{\"id\":15,\"name\":\"Buch\",\"adminOnly\":false},{\"id\":11,\"name\":\"C\",\"adminOnly\":false},{\"id\":7,\"name\":\"EINS\",\"adminOnly\":false},{\"id\":6,\"name\":\"IKID\",\"adminOnly\":false},{\"id\":3,\"name\":\"Inklusion\",\"adminOnly\":false},{\"id\":4,\"name\":\"Klasse 1\",\"adminOnly\":false},{\"id\":5,\"name\":\"Klasse 2\",\"adminOnly\":false},{\"id\":14,\"name\":\"Logik\",\"adminOnly\":false},{\"id\":1,\"name\":\"Mathematik\",\"adminOnly\":false},{\"id\":2,\"name\":\"Minikänguru\",\"adminOnly\":false},{\"id\":18,\"name\":\"Nachbau\",\"adminOnly\":true},{\"id\":17,\"name\":\"Person\",\"adminOnly\":false},{\"id\":12,\"name\":\"Serie\",\"adminOnly\":false},{\"id\":16,\"name\":\"Zeitschrift\",\"adminOnly\":false},{\"id\":13,\"name\":\"Zitat\",\"adminOnly\":false},{\"id\":8,\"name\":\"ZWEI\",\"adminOnly\":false}]";

		given()
			.when().get("/admin/deskriptoren")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

}
