// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AdminRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class RaetselResourceAuthorizationTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	void testFindRaetselMitDeskriptorenUndSuchstringUnaothorized() throws Exception {

		given()
			.when().get("admin/v2?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testFindRaetselMitDeskriptorenUndSuchstringForbidden() throws Exception {

		given()
			.when().get("admin/v2?deskriptoren=Minikänguru,A-1&suchstring=zählen&typeDeskriptoren=STRING")
			.then()
			.statusCode(403);
	}

	@Test
	void testFindRaetselMitDeskriptorenNumerischUndSuchstringUnauthorized() throws Exception {

		given()
			.when()
			.get("admin/v2?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testFindRaetselMitDeskriptorenNumerischUndSuchstringForbidden() throws Exception {

		given()
			.when()
			.get("admin/v2?deskriptoren=2,9,13&suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(403);
	}

	@Test
	void testFindRaetselMitSuchstringUnauthoriized() throws Exception {

		given()
			.when()
			.get("admin/v2?suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testFindRaetselMitSuchstringForbidden() throws Exception {

		given()
			.when()
			.get("admin/v2?suchstring=zählen&typeDeskriptoren=ORDINAL")
			.then()
			.statusCode(403);
	}

	@Test
	void testRaetselDetailsLadenUnauthorized() throws Exception {

		given()
			.when()
			.get("05645/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testRaetselDetailsLadenPublic() throws Exception {

		given()
			.when()
			.get("02622/v1")
			.then()
			.statusCode(200);
	}

	@Test
	void testRaetselAnlegenUnauthorized() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsert.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.post("v1")
				.then()
				.statusCode(401);

		}

	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testRaetselAnlegenForbidden() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadInsert.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.post("v1")
				.then()
				.statusCode(403);

		}

	}

	@Test
	void testRaetselAendernUnauthorized() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.put("v1")
				.then()
				.statusCode(401);
		}

	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testRaetselAendernForbidden() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.put("v1")
				.then()
				.statusCode(403);
		}

	}

	@Test
	void testGeneratePNGUnauthorized() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.post("PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then()
			.statusCode(401);

	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testGeneratePNGForbidden() {

		given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.post("PNG/a4c4d45e-4a81-4bde-a6a3-54464801716d/v1?layoutAntwortvorschlaege=ANKREUZTABELLE")
			.then()
			.statusCode(403);

	}

	@Test
	void testLoadImagesUnauthorized() {

		given()
			.accept(ContentType.JSON)
			.get("PNG/02610/v1")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "bolle", roles = { "STANDARD" })
	void testLoadImagesForbidden() {

		given()
			.accept(ContentType.JSON)
			.get("PNG/02610/v1")
			.then()
			.statusCode(403);
	}
}
