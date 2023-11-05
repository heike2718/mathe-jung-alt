// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageVorschau;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
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
	class GeneratePreviewTests {

		@Test
		void should_generatePreview_work() {

			// Arrange
			String pfad = "/resources/001/01121.eps";

			when(imageGeneratorService.generiereGrafikvorschau(pfad)).thenReturn(new byte[0]);
			when(fileService.fileExists(pfad)).thenReturn(Boolean.TRUE);

			// Act
			EmbeddableImageVorschau vorschau = service.generatePreview(pfad);

			// Assert
			assertNotNull(vorschau.getImage());
			assertTrue(vorschau.isExists());
			assertEquals(pfad, vorschau.getPfad());

			verify(fileService).fileExists(pfad);
			verify(imageGeneratorService).generiereGrafikvorschau(pfad);

		}

		@Test
		void should_generatePreviewReturnWarning_when_thereIsNoGrafik() {

			// Arrange
			String pfad = "/resources/001/00000.eps";

			when(fileService.fileExists(pfad)).thenReturn(Boolean.FALSE);

			// Act
			EmbeddableImageVorschau vorschau = service.generatePreview(pfad);

			// Assert
			assertNull(vorschau.getImage());
			assertFalse(vorschau.isExists());
			assertEquals(pfad, vorschau.getPfad());

			verify(fileService).fileExists(pfad);
			verify(imageGeneratorService, never()).generiereGrafikvorschau(pfad);

		}

		@Test
		void should_generatePreviewReturnErrorMessage_when_ExceptionBeimGenerieren() {

			// Arrange
			String pfad = "/resources/001/01121.eps";

			when(imageGeneratorService.generiereGrafikvorschau(pfad))
				.thenThrow(new RuntimeException("irgendwas ist schiefgelaufen"));
			when(fileService.fileExists(pfad)).thenReturn(Boolean.TRUE);

			// Act
			EmbeddableImageVorschau vorschau = service.generatePreview(pfad);

			assertNull(vorschau.getImage());
			assertTrue(vorschau.isExists());
			assertEquals(pfad, vorschau.getPfad());

			verify(fileService).fileExists(pfad);
			verify(imageGeneratorService).generiereGrafikvorschau(pfad);

		}
	}
}
