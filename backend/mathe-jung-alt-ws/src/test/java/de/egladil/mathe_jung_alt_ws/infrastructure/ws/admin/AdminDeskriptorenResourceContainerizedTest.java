// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

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
@TestHTTPEndpoint(AdminDeskriptorenResource.class)
@TestProfile(ContainerDatabaseTestProfile.class)
public class AdminDeskriptorenResourceContainerizedTest {

	@Test
	public void testLoadDeskriptorenEndpoint() {

		String expected = "[{\"id\":9,\"name\":\"A\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":10,\"name\":\"B\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":11,\"name\":\"C\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":7,\"name\":\"EINS\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":6,\"name\":\"IKID\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":3,\"name\":\"Inklusion\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":4,\"name\":\"Klasse 1\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":5,\"name\":\"Klasse 2\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":14,\"name\":\"Logik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":1,\"name\":\"Mathematik\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":2,\"name\":\"Minikänguru\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":29,\"name\":\"Multiple Choice\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":12,\"name\":\"Serie\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":13,\"name\":\"Zitat\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":8,\"name\":\"ZWEI\",\"admin\":true,\"kontext\":\"RAETSEL\"}]";

		given()
			.when().get("?kontext=RAETSEL")
			.then()
			.statusCode(200)
			.body(is(expected));
	}

}
