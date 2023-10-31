// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageVorschau;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.upload.UploadedFile;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * EmbeddableImageServiceTest
 */
@QuarkusTest
public class EmbeddableImageServiceTest {

	@InjectMock
	RaetselFileService fileService;

	@InjectMock
	ImageGeneratorService imageGeneratorService;

	@Inject
	EmbeddableImageService service;

	@Nested
	class FindGrafikTests {

		@Test
		void should_findGrafik_work() {

			// Arrange
			String pfad = "/resources/001/01121.eps";

			when(imageGeneratorService.generiereGrafikvorschau(pfad)).thenReturn(new byte[0]);
			when(fileService.existsGrafik(pfad)).thenReturn(Boolean.TRUE);

			// Act
			EmbeddableImageVorschau grafik = service.generatePreview(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertTrue(messagePayload.isOk());
			assertNotNull(grafik.getImage());

			verify(fileService).existsGrafik(pfad);
			verify(imageGeneratorService).generiereGrafikvorschau(pfad);

		}

		@Test
		void should_findGrafikReturnErrorMessage_when_invalidPath() {

			// Arrange
			String pfad = "/ressources/001/01121.eps";

			// Act
			EmbeddableImageVorschau grafik = service.generatePreview(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Aufruf mit ungültigem Pfad", messagePayload.getMessage());
			assertNull(grafik.getImage());

			verify(fileService, never()).existsGrafik(pfad);
			verify(imageGeneratorService, never()).generiereGrafikvorschau(pfad);

		}

		@Test
		void should_findGrafikReturnWarning_when_thereIsNoGrafik() {

			// Arrange
			String pfad = "/resources/001/00000.eps";

			when(fileService.existsGrafik(pfad)).thenReturn(Boolean.FALSE);

			// Act
			EmbeddableImageVorschau grafik = service.generatePreview(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertEquals("WARN", messagePayload.getLevel());
			assertEquals("Falls der Pfad stimmt, wurde die Datei noch nicht hochgeladen.",
				messagePayload.getMessage());
			assertNull(grafik.getImage());

			verify(fileService).existsGrafik(pfad);
			verify(imageGeneratorService, never()).generiereGrafikvorschau(pfad);

		}

		@Test
		void should_findGrafikReturnErrorMessage_when_ExceptionBeimGenerieren() {

			// Arrange
			String pfad = "/resources/001/01121.eps";

			when(imageGeneratorService.generiereGrafikvorschau(pfad))
				.thenThrow(new RuntimeException("irgendwas ist schiefgelaufen"));
			when(fileService.existsGrafik(pfad)).thenReturn(Boolean.TRUE);

			// Act
			EmbeddableImageVorschau grafik = service.generatePreview(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertEquals("WARN", messagePayload.getLevel());
			assertEquals("Die EmbeddableImageVorschau existiert zwar, aber beim Umwandeln in png lief etwas schief.",
				messagePayload.getMessage());
			assertNull(grafik.getImage());

			verify(fileService).existsGrafik(pfad);
			verify(imageGeneratorService).generiereGrafikvorschau(pfad);

		}
	}

	@Nested
	class GrafikSpeichernTests {

		@Test
		void should_findGrafikReturnErrorMessage_when_relativePathNull() throws Exception {

			// Arrange
			File file = File.createTempFile("00000-", "eps");
			file.deleteOnExit();

			ReplaceEmbeddableImageRequestDto uploadRequestDto = new ReplaceEmbeddableImageRequestDto()
				.withUpload(
					new UploadedFile().withName("00000.eps").withData(MjaFileUtils.loadBinaryFile(file.getAbsolutePath(), false)));

			// Act
			MessagePayload messagePayload = service.replaceEmbeddedImage(uploadRequestDto);

			// Assert

			System.out.println(messagePayload.toString());

			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Aufruf ohne Pfad", messagePayload.getMessage());

		}

		@Test
		void should_findGrafikReturnErrorMessage_when_invalidPath() throws Exception {

			// Arrange
			File file = File.createTempFile("00000-", "eps");
			file.deleteOnExit();

			String pfad = "/ressources/001/01121.eps";
			ReplaceEmbeddableImageRequestDto uploadRequestDto = new ReplaceEmbeddableImageRequestDto()
				.withRelativerPfad(pfad)
				.withUpload(
					new UploadedFile().withName("00000.eps").withData(MjaFileUtils.loadBinaryFile(file.getAbsolutePath(), false)));

			// Act
			MessagePayload messagePayload = service.replaceEmbeddedImage(uploadRequestDto);

			// Assert

			System.out.println(messagePayload.toString());

			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Aufruf mit ungültigem Pfad", messagePayload.getMessage());

		}

	}

}
