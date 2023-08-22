// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.profiles.OpenDataTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * QuizzResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuizzResource.class)
@TestProfile(OpenDataTestProfile.class)
public class QuizzResourceTest {

	@Test
	void testGetQuizzMinikaenguru() {

		given()
			.when().get("MINIKAENGURU/2020/EINS")
			.then()
			.statusCode(200);
	}

}
