// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;

import org.jboss.resteasy.reactive.server.core.multipart.FormData;

import de.egladil.mja_api.TestFileUtils;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * UploadResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(UploadResource.class)
public class UploadResourceTest {

	// @Test
	// Es gibt einen bug: https://github.com/rest-assured/rest-assured/issues/841
	void shouldUploadFile_work() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00001.eps");

		FormData formData = new FormData(1);
		formData.add("uploadedFile", data, "00001.eps", null);

		given().when()
			.contentType(ContentType.URLENC)
			.header("Accept", ContentType.JSON)
			.formParams("pfad", "/resources/003/00001.eps")
			.formParams("uploadedFile", formData)
			.post("v1")
			.then()
			.statusCode(200);

	}

}
