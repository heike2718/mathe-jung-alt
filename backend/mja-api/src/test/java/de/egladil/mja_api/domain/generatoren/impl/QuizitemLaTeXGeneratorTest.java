// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.generatoren.TrennerartFrageLoesung;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import io.quarkus.test.junit.QuarkusTest;

/**
 * QuizitemLaTeXGeneratorTest
 */
@QuarkusTest
public class QuizitemLaTeXGeneratorTest {

	@Nested
	class GenerateLaTeXFrageLoesungTests {

		@Test
		void should_generateLaTeXFrageLoesung_work_whenMultipleChoiceAndMoreThan0Antwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertTrue(result.contains("A-1"));
			assertTrue(result.contains("00756"));
			assertTrue(result.contains("Lösung ist B (b)"));
			assertTrue(result.contains("green"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertTrue(result.contains("vspace{1ex}"));
			assertTrue(result.contains("begin{tabular}{*{2}{|C{2cm}}|}"));
			assertTrue(result.contains(" (300) }"));
			assertTrue(result.contains("bf Lösung A-1 - 00756"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenKeinKorrekterAntwortvorschlag() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b");

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			System.out.println(result);

			// Assert
			assertTrue(result.contains("A-1"));
			assertTrue(result.contains("00756"));
			assertFalse(result.contains("Lösung ist B (b)"));
			assertTrue(result.contains("green"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("{loesungsbuchstabe}"));
			assertTrue(result.contains("vspace{1ex}"));
			assertTrue(result.contains("begin{tabular}{*{2}{|C{2cm}}|}"));
			assertTrue(result.contains(" (300) }"));
			assertTrue(result.contains("bf Lösung A-1 - 00756"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenLoesungBlank() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung(" ")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertTrue(result.contains("A-1"));
			assertTrue(result.contains("00756"));
			assertFalse(result.contains("Lösung"));
			assertTrue(result.contains("green"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("vspace{1ex}"));
			assertTrue(result.contains("begin{tabular}{*{2}{|C{2cm}}|}"));
			assertTrue(result.contains(" (300) }"));
			assertFalse(result.contains("bf Lösung A-1 - 00756"));
			assertFalse(result.contains("{trenner-frage-loesung}"));
			assertFalse(result.contains("{header-loesung}"));
			assertFalse(result.contains("{loesungsbuchstabe}"));
			assertFalse(result.contains("{content-loesung}"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenNotMultipleChoice() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = false;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("Lösung ist B (b)"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenNoAntwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("Lösung ist B (b)"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenAntwortvorschlaegeNull() {

			// Arrange
			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("Lösung ist B (b)"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenVerwendungszweckLatex() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.LATEX);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.ABSTAND, printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("Lösung ist B (b)"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));
			assertFalse(result.contains("00756"));
			assertFalse(result.contains("green"));

		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenNummerEqualsSchluessel() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.DESCRIPTION)
				.withLoesung("ganz klar B")
				.withNummer("00756")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			QuizitemLaTeXGenerator generator = new QuizitemLaTeXGenerator();

			// Act
			String result = generator.generateLaTeXFrageLoesung(input, TrennerartFrageLoesung.SEITENUMBRUCH, printAsMultipleChoice);

			System.out.println(result);

			// Assert
			assertFalse(result.contains("Lösung ist B (b)"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));
			assertTrue(result.contains("00756"));
			assertTrue(result.contains("green"));
			assertTrue(result.contains("(300)"));
			assertTrue(result.contains("bf Lösung 00756"));

		}

	}

	@Nested
	class GenerateLaTeXFrageTests {

		@Test
		void should_generateLaTeXFrageLoesung_work_whenMultipleChoiceAndMoreThan0Antwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXFrage(input,
				printAsMultipleChoice);

			System.out.println(result);

			// Assert
			assertTrue(result.contains("A-1"));
			assertTrue(result.contains("00756"));
			assertTrue(result.contains("green"));
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("vspace{1ex}"));
			assertTrue(result.contains("begin{tabular}{*{2}{|C{2cm}}|}"));
			assertTrue(result.contains(" (300) }"));
			assertFalse(result.contains("bf Lösung A-1 - 00756"));
			assertFalse(result.contains("Lösung ist B (b)"));
		}

		@Test
		void should_generateLaTeXFrage_work_whenNoAntwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXFrage(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));
		}

		@Test
		void should_generateLaTeXFrage_work_whenAntwortvorschlaegeNull() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = null;

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXFrage(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));
		}

		@Test
		void should_generateLaTeXFrageLoesung_work_whenNotMultipleChoiceAndMoreThan0Antwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = false;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXFrage(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("{antwortvorschlaege}"));
			assertFalse(result.contains("tabular"));
		}
	}

	@Nested
	class GenerateLaTeXLoesungTests {

		@Test
		void should_generateLaTeXLoesung_work_whenMultipleChoiceAndMoreThan0Antwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withLoesung("ganz klar B")
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXLoesung(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertTrue(result.contains("00756"));
			assertTrue(result.contains("bf Lösung A-1 - 00756"));
			assertTrue(result.contains("it Lösung ist B (b)}"));
		}

		@Test
		void should_generateLaTeXLoesung_work_whenNoAntwortvorschlaege() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLoesung("ganz klar B")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXLoesung(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("{header-loesung}"));
			assertFalse(result.contains("{loesungsbuchstabe}"));
			assertFalse(result.contains("{content-loesung}"));
		}

		@Test
		void should_generateLaTeXLoesung_work_whenAntwortvorschlaegeNull() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = null;

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLoesung("ganz klar B")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXLoesung(input,
				printAsMultipleChoice);

			// System.out.println(result);

			// Assert
			assertFalse(result.contains("{header-loesung}"));
			assertFalse(result.contains("{loesungsbuchstabe}"));
			assertFalse(result.contains("{content-loesung}"));
		}

		@Test
		void should_generateLaTeXLoesung_work_whenLoesungBlank() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[0];

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = true;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXLoesung(input,
				printAsMultipleChoice);

			assertEquals("", result);
		}

		@Test
		void should_generateLaTeXLoesung_work_whenNotPrintAsMultipleChoice() {

			// Arrange
			Antwortvorschlag[] antwortvorschlaege = new Antwortvorschlag[2];
			antwortvorschlaege[0] = new Antwortvorschlag().withBuchstabe("A").withText("a");
			antwortvorschlaege[1] = new Antwortvorschlag().withBuchstabe("B").withText("b").withKorrekt(true);

			RaetselGeneratorinput input = new RaetselGeneratorinput()
				.withAntwortvorschlaege(antwortvorschlaege)
				.withFrage("Was stimmt?")
				.withLoesung("ganz klar B")
				.withLayoutAntwortvorschlaege(LayoutAntwortvorschlaege.ANKREUZTABELLE)
				.withNummer("A-1")
				.withPunkten(300)
				.withSchluessel("00756")
				.withVerwendungszweck(Verwendungszweck.VORSCHAU);

			boolean printAsMultipleChoice = false;

			// Act
			String result = new QuizitemLaTeXGenerator().generateLaTeXLoesung(input,
				printAsMultipleChoice);

			System.out.println(result);

			assertTrue(result.contains("bf Lösung A-1 - 00756"));
			assertFalse(result.contains("{loesungsbuchstabe}"));
			assertTrue(result.contains("ganz klar B"));
		}
	}
}
