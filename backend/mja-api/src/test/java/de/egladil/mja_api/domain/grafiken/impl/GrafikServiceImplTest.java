// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.grafiken.impl;

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
import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.generatoren.ImageGeneratorService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.grafiken.Grafik;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * GrafikServiceImplTest
 */
@QuarkusTest
public class GrafikServiceImplTest {

	@InjectMock
	RaetselFileService fileService;

	@InjectMock
	ImageGeneratorService imageGeneratorService;

	@Inject
	GrafikServiceImpl service;

	@Nested
	class FindGrafikTests {

		@Test
		void should_findGrafik_work() {

			// Arrange
			String pfad = "/resources/001/01121.eps";

			when(imageGeneratorService.generiereGrafikvorschau(pfad)).thenReturn(new byte[0]);
			when(fileService.existsGrafik(pfad)).thenReturn(Boolean.TRUE);

			// Act
			Grafik grafik = service.findGrafik(pfad);

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
			Grafik grafik = service.findGrafik(pfad);

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
			Grafik grafik = service.findGrafik(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertEquals("WARN", messagePayload.getLevel());
			assertEquals("Falls der Pfad stimmt, wurde die Grafik noch nicht hochgeladen.", messagePayload.getMessage());
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
			Grafik grafik = service.findGrafik(pfad);

			// Assert
			MessagePayload messagePayload = grafik.getMessagePayload();

			// System.out.println(messagePayload.toString());

			assertEquals("WARN", messagePayload.getLevel());
			assertEquals("Die Grafik existiert zwar, aber beim Umwandeln in png lief etwas schief.", messagePayload.getMessage());
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

			UploadRequestDto uploadRequestDto = new UploadRequestDto()
				.withBenutzerUuid("uuid")
				.withUploadData(new UploadData("00000.eps", file));

			// Act
			MessagePayload messagePayload = service.grafikSpeichern(uploadRequestDto);

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
			UploadRequestDto uploadRequestDto = new UploadRequestDto()
				.withBenutzerUuid("uuid")
				.withRelativerPfad(pfad)
				.withUploadData(new UploadData("00000.eps", file));

			// Act
			MessagePayload messagePayload = service.grafikSpeichern(uploadRequestDto);

			// Assert

			System.out.println(messagePayload.toString());

			assertEquals("ERROR", messagePayload.getLevel());
			assertEquals("Aufruf mit ungültigem Pfad", messagePayload.getMessage());

		}

	}

}