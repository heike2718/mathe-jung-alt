// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.TestFileUtils;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.embeddable_images.dto.CreateEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageVorschau;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.Textart;
import de.egladil.mja_api.domain.upload.UploadedFile;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * EmbeddableImagesResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(EmbeddableImagesResource.class)
@TestProfile(FullDatabaseTestProfile.class)
public class EmbeddableImagesResourceTest {

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldCreateEmbeddableImage_work() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("neu").withTextart(Textart.FRAGE);
		CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
		requestDto.setContext(context);
		requestDto.setFile(uploadedFile);

		EmbeddableImageResponseDto result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(200)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(EmbeddableImageResponseDto.class);

		System.out.println(latexBaseDir + result.getPfad());
		System.out.println(result.getIncludegraphicsCommand());

		assertEquals(context, result.getContext());
		assertTrue(result.getIncludegraphicsCommand().length() > 0);
		assertFalse(result.getPfad().isEmpty());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldCreateEmbeddableImageReturn400_when_raetselIdInvalid() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("abcx").withTextart(Textart.FRAGE);
		CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
		requestDto.setContext(context);
		requestDto.setFile(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("die raetselId enthält ungültige Zeichen", result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void shouldCreateEmbeddableImage_rejectExcel() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/excel.xlsx");

		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("neu").withTextart(Textart.FRAGE);
		CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
		requestDto.setContext(context);
		requestDto.setFile(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("Die hochgeladene Datei kann nicht verarbeitet werden. Bitte senden Sie eine Mail an info@egladil.de.",
			result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "AUTOR" })
	void shouldCreateEmbeddableImage_rejectVirus() throws Exception {

		// Für den RestAssured-Test muss man das decoden, weil UploadedFile es encoded.
		byte[] content = Base64.getDecoder()
			.decode("WDVPIVAlQEFQWzRcUFpYNTQoUF4pN0NDKTd9JEVJQ0FSLVNUQU5EQVJELUFOVElWSVJVUy1URVNULUZJTEUhJEgrSCo=".getBytes());

		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(content);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("neu").withTextart(Textart.FRAGE);
		CreateEmbeddableImageRequestDto requestDto = new CreateEmbeddableImageRequestDto();
		requestDto.setContext(context);
		requestDto.setFile(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("Die hochgeladene Datei kann nicht verarbeitet werden. Bitte prüfen Sie Ihren Rechner auf Viren.",
			result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImage_work() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		String raetselId = "69959982-83f9-482d-a26c-8eb4a92bd6ff";
		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(raetselId).withTextart(Textart.FRAGE);

		String relativerPfad = "/resources/001/01003.eps";
		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withRelativerPfad(relativerPfad).withUpload(uploadedFile);

		EmbeddableImageResponseDto result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.post("v1")
			.then()
			.statusCode(200)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(EmbeddableImageResponseDto.class);

		assertEquals(context, result.getContext());
		assertTrue(result.getIncludegraphicsCommand().length() > 0);
		assertFalse(result.getPfad().isEmpty());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImageReturn400_when_raetselIdInvalid() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("abcx").withTextart(Textart.FRAGE);

		String relativerPfad = "/resources/001/01003.eps";

		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withRelativerPfad(relativerPfad).withUpload(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("die raetselId enthält ungültige Zeichen", result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImageReturn400_when_pfadNull() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("abcdef").withTextart(Textart.FRAGE);

		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withUpload(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("die raetselId enthält ungültige Zeichen", result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImageReturn400_when_pfadInvalid() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/00000.eps");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId("abcx").withTextart(Textart.FRAGE);

		String relativerPfad = "/resources/e/001/01003.eps";

		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withRelativerPfad(relativerPfad).withUpload(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.put("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("die raetselId enthält ungültige Zeichen", result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImage_rejectExcel() throws Exception {

		// Arrange
		byte[] data = TestFileUtils.loadBytes("/eps/excel.xlsx");
		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(data);

		String relativerPfad = "/resources/001/01003.eps";
		String raetselId = "69959982-83f9-482d-a26c-8eb4a92bd6ff";
		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(raetselId).withTextart(Textart.FRAGE);

		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withRelativerPfad(relativerPfad).withUpload(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.post("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("Die hochgeladene Datei kann nicht verarbeitet werden. Bitte senden Sie eine Mail an info@egladil.de.",
			result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void shouldReplaceEmbeddableImage_rejectVirus() throws Exception {

		// Arrange
		// Für den RestAssured-Test muss man das decoden, weil UploadedFile es encoded.
		byte[] content = Base64.getDecoder()
			.decode("WDVPIVAlQEFQWzRcUFpYNTQoUF4pN0NDKTd9JEVJQ0FSLVNUQU5EQVJELUFOVElWSVJVUy1URVNULUZJTEUhJEgrSCo=".getBytes());

		UploadedFile uploadedFile = new UploadedFile().withName("00000.eps")
			.withData(content);

		String relativerPfad = "/resources/001/01003.eps";
		String raetselId = "69959982-83f9-482d-a26c-8eb4a92bd6ff";
		EmbeddableImageContext context = new EmbeddableImageContext().withRaetselId(raetselId).withTextart(Textart.FRAGE);

		ReplaceEmbeddableImageRequestDto requestDto = new ReplaceEmbeddableImageRequestDto().withContext(context)
			.withRelativerPfad(relativerPfad).withUpload(uploadedFile);

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.body(requestDto)
			.post("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("Die hochgeladene Datei kann nicht verarbeitet werden. Bitte prüfen Sie Ihren Rechner auf Viren.",
			result.getMessage());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void should_generatePreview_work() {

		String relativerPfad = "/resources/001/01003.eps";

		EmbeddableImageVorschau result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.queryParam("pfad", relativerPfad)
			.get("v1")
			.then()
			.statusCode(200)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(EmbeddableImageVorschau.class);

		assertTrue(result.getImage().length > 0);
		assertEquals(relativerPfad, result.getPfad());
		assertTrue(result.isExists());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void should_generatePreview_work_when_resourceNotFound() {

		String relativerPfad = "/resources/001/90000.eps";

		EmbeddableImageVorschau result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.queryParam("pfad", relativerPfad)
			.get("v1")
			.then()
			.statusCode(200)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(EmbeddableImageVorschau.class);

		assertNull(result.getImage());
		assertEquals(relativerPfad, result.getPfad());
		assertFalse(result.isExists());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "ADMIN" })
	void should_generatePreview_return400_when_pfadInvalid() {

		String relativerPfad = "/resources/x/00786.eps";

		MessagePayload result = given().when()
			.contentType(ContentType.JSON)
			.header("Accept", ContentType.JSON)
			.queryParam("pfad", relativerPfad)
			.get("v1")
			.then()
			.statusCode(400)
			.and()
			.assertThat()
			.contentType(ContentType.JSON)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", result.getLevel());
		assertEquals("pfad enthält ungültige Zeichen", result.getMessage());
	}

}
