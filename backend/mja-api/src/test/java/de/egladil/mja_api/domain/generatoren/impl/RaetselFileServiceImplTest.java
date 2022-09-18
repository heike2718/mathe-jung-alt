// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.TestFileUtils;
import de.egladil.mja_api.domain.generatoren.impl.RaetselFileServiceImpl;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * RaetselFileServiceImplTest
 */
@QuarkusTest
@TestProfile(FullDatabaseTestProfile.class)
public class RaetselFileServiceImplTest {

	private static final String BASE_DIR = "/home/heike/test";

	RaetselFileServiceImpl fileService;

	@BeforeEach
	void setUp() {

		fileService = new RaetselFileServiceImpl();
		fileService.setLatexBaseDir(BASE_DIR);
	}

	@Nested
	class GenerateFrageLaTeXTests {

		@Test
		void should_generateFrageLaTeX_WriteCompilableLaTeXFile() throws Exception {

			// Arrange
			String expectedPath = BASE_DIR + File.separator + "generator-test.tex";

			File file = new File(expectedPath);

			if (file.exists() && file.isFile()) {

				file.delete();
			}

			try (InputStream in = getClass().getResourceAsStream("/payloads/Raetsel.json")) {

				Raetsel raetsel = new ObjectMapper().readValue(in, Raetsel.class);

				// Act
				String path = fileService.generateFrageLaTeX(raetsel, LayoutAntwortvorschlaege.NOOP);

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
			String expectedPath = BASE_DIR + File.separator + "generator-test.tex";

			File file = new File(expectedPath);

			if (file.exists() && file.isFile()) {

				file.delete();
			}

			Raetsel raetsel = TestFileUtils.loadReaetsel();

			// Act
			String path = fileService.generateFrageLaTeX(raetsel, LayoutAntwortvorschlaege.BUCHSTABEN);

			// Assert
			assertEquals(expectedPath, path);
			File result = new File(path);
			assertTrue(result.isFile());
			assertTrue(result.canRead());
		}
	}

	@Nested
	class GenerateLoesungLaTeXTests {

		@Test
		void should_generateLoesungLaTeX_WriteCompilableLaTeXFile() throws Exception {

			// Arrange
			String expectedPath = BASE_DIR + File.separator + "generator-test_l.tex";

			File file = new File(expectedPath);

			if (file.exists() && file.isFile()) {

				file.delete();
			}

			try (InputStream in = getClass().getResourceAsStream("/payloads/Raetsel.json")) {

				Raetsel raetsel = new ObjectMapper().readValue(in, Raetsel.class);

				// Act
				String path = fileService.generateLoesungLaTeX(raetsel);

				// Assert
				assertEquals(expectedPath, path);
				File result = new File(path);
				assertTrue(result.isFile());
				assertTrue(result.canRead());
			}
		}

	}

	@Nested
	class TextLoesungsbuchstabenTests {

		@Test
		void should_getTextLoesungsbuchstabeReturnBlank_when_antwortvorschlaegeNull() {

			// Arrange
			String expected = "";

			// Act
			String result = fileService.getTextLoesungsbuchstabe(null);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnBlank_when_antwortvorschlaegeLeer() {

			// Arrange
			String expected = "";

			// Act
			String result = fileService.getTextLoesungsbuchstabe(new Antwortvorschlag[0]);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnBlank_when_keinerKorrekt() {

			// Arrange
			String expected = "";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnLoesungsbuchstabeUndText_when_textDifferent() {

			// Arrange
			String expected = "{\\bf Lösung ist X (42)}\\par \\vspace{1ex}";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("42");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textEquals() {

			// Arrange
			String expected = "{\\bf Lösung ist X}\\par \\vspace{1ex}";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("X");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textNull() {

			// Arrange
			String expected = "{\\bf Lösung ist X}\\par \\vspace{1ex}";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText(null);

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textBlank() {

			// Arrange
			String expected = "{\\bf Lösung ist X}\\par \\vspace{1ex}";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("  ");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);

			// Assert
			assertEquals(expected, result);
		}

	}
}
