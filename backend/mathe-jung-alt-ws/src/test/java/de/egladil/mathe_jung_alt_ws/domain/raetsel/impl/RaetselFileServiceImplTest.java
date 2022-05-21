// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * RaetselFileServiceImplTest
 */
public class RaetselFileServiceImplTest {

	private static final String BASE_DIR = "/media/veracrypt1/knobelarchiv_2/latex/temp";

	RaetselFileServiceImpl fileService;

	@BeforeEach
	void setUp() {

		fileService = new RaetselFileServiceImpl();
		fileService.setLatexBaseDir(BASE_DIR);
	}

	@Test
	void should_generateLaTeXDocumentOfRaetselFrageWriteCompilableLaTeXFile() throws Exception {

		// Arrange
		String expectedPath = BASE_DIR + File.separator + "02789.tex";

		File file = new File(expectedPath);

		if (file.exists() && file.isFile()) {

			file.delete();
		}

		try (InputStream in = getClass().getResourceAsStream("/payloads/Raetsel.json")) {

			Raetsel raetsel = new ObjectMapper().readValue(in, Raetsel.class);

			// Act
			String path = fileService.generateLaTeXDocumentOfRaetselFrage(raetsel);

			// Assert
			assertEquals(expectedPath, path);
			File result = new File(path);
			assertTrue(result.isFile());
			assertTrue(result.canRead());
		}
	}
}
