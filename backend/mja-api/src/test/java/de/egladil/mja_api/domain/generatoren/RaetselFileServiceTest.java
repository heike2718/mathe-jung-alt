// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.TestFileUtils;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * RaetselFileServiceTest
 */
@QuarkusTest
@TestProfile(FullDatabaseAdminTestProfile.class)
public class RaetselFileServiceTest {

	private static final String BASE_DIR = "/home/heike/test";

	RaetselFileService fileService;

	@BeforeEach
	void setUp() {

		fileService = new RaetselFileService();
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
				String path = fileService.generateFrageLaTeX(raetsel, LayoutAntwortvorschlaege.NOOP, FontName.STANDARD,
					Schriftgroesse.NORMAL);

				// Assert
				assertEquals(expectedPath, path);
				File result = new File(path);
				assertTrue(result.isFile());
				assertTrue(result.canRead());
			}
		}

		@Test
		void should_generateFrageLaTeX_add_Antworten_whenNichtEingebettet() throws Exception {

			// Arrange
			String expectedPath = BASE_DIR + File.separator + "generator-test.tex";

			File file = new File(expectedPath);

			if (file.exists() && file.isFile()) {

				file.delete();
			}

			Raetsel raetsel = TestFileUtils.loadReaetsel();

			// Act
			String path = fileService.generateFrageLaTeX(raetsel, LayoutAntwortvorschlaege.BUCHSTABEN, FontName.STANDARD,
				Schriftgroesse.NORMAL);

			// Assert
			assertEquals(expectedPath, path);
			File result = new File(path);
			assertTrue(result.isFile());
			assertTrue(result.canRead());

			String text = MjaFileUtils.readTextFile(path);

			// System.out.println(text);

			assertNotNull(text);
			assertTrue(text.contains("\\end{tabular}\\end{center}}\\end{document}"));
		}

		@Test
		void should_generateFrageLaTeX_not_Antworten_whenEingebettet() throws Exception {

			// Arrange
			String expectedPath = BASE_DIR + File.separator + "generator-test.tex";

			File file = new File(expectedPath);

			if (file.exists() && file.isFile()) {

				file.delete();
			}

			Raetsel raetsel = TestFileUtils.loadReaetsel().withAntwortvorschlaegeEingebettet(true);

			// Act
			String path = fileService.generateFrageLaTeX(raetsel, LayoutAntwortvorschlaege.BUCHSTABEN, FontName.STANDARD,
				Schriftgroesse.NORMAL);

			// Assert
			assertEquals(expectedPath, path);
			File result = new File(path);
			assertTrue(result.isFile());
			assertTrue(result.canRead());

			String text = MjaFileUtils.readTextFile(path);

			// System.out.println(text);

			assertNotNull(text);
			assertFalse(text.contains("\\end{tabular}\\end{center}}\\end{document}"));
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
				String path = fileService.generateLoesungLaTeX(raetsel, FontName.STANDARD, Schriftgroesse.NORMAL);

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
			String expected = "{\\it Lösung ist X (42)}\\par";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("42");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);
			// System.out.println(result);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textEquals() {

			// Arrange
			String expected = "{\\it Lösung ist X}\\par";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("X");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);
			// System.out.println(result);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textNull() {

			// Arrange
			String expected = "{\\it Lösung ist X}\\par";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText(null);

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);
			// System.out.println(result);

			// Assert
			assertEquals(expected, result);
		}

		@Test
		void should_getTextLoesungsbuchstabeReturnNurLoesungsbuchstabe_when_textBlank() {

			// Arrange
			String expected = "{\\it Lösung ist X}\\par";

			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[1];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("X").withKorrekt(true).withText("  ");

			// Act
			String result = fileService.getTextLoesungsbuchstabe(antwortvorschlaege);
			// System.out.println(result);

			// Assert
			assertEquals(expected, result);
		}

	}
}
