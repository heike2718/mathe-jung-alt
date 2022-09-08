// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.infrastructure.resources.AufgabensammlungResource;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

/**
 * AufgabensammlungResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(AufgabensammlungResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class AufgabensammlungResourceTest {

	@Test
	void testGetAufgabensammlungKeinTreffer() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/Serie 2000/EINS");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungVulnerableStringInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/cd ..%2f../EINS");

		// Assert
		assertEquals(400, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungInvalidReferenztypInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/cd ..%2f../2005/EINS");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

	@Test
	void testGetAufgabensammlungInvalidSchwierigkeitsgradInput() throws Exception {

		// Act
		Response response = given()
			.when().get("/SERIE/2005/HUN_DERT_JAEH_RI_GE_UND_AELTER");

		// Assert
		assertEquals(404, response.getStatusCode());

	}

}
