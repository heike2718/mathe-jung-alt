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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.profiles.FullDatabaseAutorTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AutorRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseAutorTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AutorRaetselResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(1)
	void should_raetselAendernReturn403_when_notTheOwner() throws Exception {

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
}
