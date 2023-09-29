// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

/**
 * QuizzResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(QuizzResource.class)
public class QuizzResourceTest {

	@Test
	void testGetQuizzMinikaenguru() {

		given()
			.when().get("MINIKAENGURU/2020/EINS/v1")
			.then()
			.statusCode(200);
	}

	@Test
	void testGetAufgabensammlungKeinTreffer() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/Serie 2000/EINS/v1");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungVulnerableStringInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/cd ..%2f../EINS/v1");

		// Assert
		assertEquals(400, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungInvalidReferenztypInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/cd ..%2f../2005/EINS/v1");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungInvalidSchwierigkeitsgradInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/2005/HUN_DERT_JAEH_RI_GE_UND_AELTER/v1");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

}
