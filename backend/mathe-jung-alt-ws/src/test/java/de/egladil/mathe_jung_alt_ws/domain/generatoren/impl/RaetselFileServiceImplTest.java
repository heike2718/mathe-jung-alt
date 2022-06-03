// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.TestFileUtils;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import io.quarkus.test.junit.QuarkusTest;

/**
 * RaetselFileServiceImplTest
 */
@QuarkusTest
public class RaetselFileServiceImplTest {

	private static final String BASE_DIR = "/home/heike/test";

	RaetselFileServiceImpl fileService;

	@BeforeEach
	void setUp() {

		fileService = new RaetselFileServiceImpl();
		fileService.setLatexBaseDir(BASE_DIR);
	}

	@Test
	void should_generateFrageLaTeX_WriteCompilableLaTeXFile() throws Exception {

		// Arrange
		String expectedPath = BASE_DIR + File.separator + "02789.tex";

		File file = new File(expectedPath);

		if (file.exists() && file.isFile()) {

			file.delete();
		}

		try (InputStream in = getClass().getResourceAsStream("/payloads/Raetsel.json")) {

			Raetsel raetsel = new ObjectMapper().readValue(in, Raetsel.class);

			// Act
			String path = fileService.generateFrageLaTeX(raetsel, Outputformat.PDF, LayoutAntwortvorschlaege.NOOP);

			// Assert
			assertEquals(expectedPath, path);
			File result = new File(path);
			assertTrue(result.isFile());
			assertTrue(result.canRead());
		}
	}

	@Test
	void should_generateFrageLaTeX_add_Antworten() throws Exception {

		// Arrange
		String expectedPath = BASE_DIR + File.separator + "02789.tex";

		File file = new File(expectedPath);

		if (file.exists() && file.isFile()) {

			file.delete();
		}

		Raetsel raetsel = TestFileUtils.loadReaetsel();

		// Act
		String path = fileService.generateFrageLaTeX(raetsel, Outputformat.PDF, LayoutAntwortvorschlaege.NOOP);

		// Assert
		assertEquals(expectedPath, path);
		File result = new File(path);
		assertTrue(result.isFile());
		assertTrue(result.canRead());
	}
}
