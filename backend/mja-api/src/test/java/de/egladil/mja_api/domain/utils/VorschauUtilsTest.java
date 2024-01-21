// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * VorschauUtilsTest
 */
public class VorschauUtilsTest {

	@Test
	void should_getVorschautextCuttOffStartingTIKZEnvironment() {

		// Arrange
		String text = """

						\\begin{tikzpicture}
			\\node at (0.6,0.5) {\\Large {\\usefont{U}{dice3d}{m}{n} 2b}};
			\\node at (0,0) {\\Large {\\usefont{U}{dice3d}{m}{n} 3d}};
			\\end{tikzpicture}
			Hier ist der eigentliche Text der Aufgabe.
						""";

		String expected = "Hier ist der eigentliche Text der Aufgabe.";

		// Act
		String actual = VorschauUtils.getVorschautext(text, 100);

		// Assert
		assertEquals(expected, actual);

	}

	@Test
	void should_getVorschautextCuttOffStartingCentederTIKZEnvironment() {

		// Arrange
		String text = """
						\\begin{center}
			\\begin{tikzpicture}
			\\node at (0.6,0.5) {\\Large {\\usefont{U}{dice3d}{m}{n} 2b}};
			\\node at (0,0) {\\Large {\\usefont{U}{dice3d}{m}{n} 3d}};
			\\end{tikzpicture}
			\\end{center}
			Hier ist der eigentliche Text der Aufgabe.
						""";

		String expected = "Hier ist der eigentliche Text der Aufgabe.";

		// Act
		String actual = VorschauUtils.getVorschautext(text, 100);

		// Assert
		assertEquals(expected, actual);

	}

	@Test
	void should_getVorschautextCuttOffStartingEmbeddedImage() {

		// Arrange
		String text = """
						\\begin{center}
			\\includegraphics[width=0.75\\linewidth]{./resources/003/01159.eps}
			\\end{center}
			Hier ist der eigentliche Text der Aufgabe.
			""";

		String expected = "Hier ist der eigentliche Text der Aufgabe.";

		// Act
		String actual = VorschauUtils.getVorschautext(text, 100);

		// Assert
		assertEquals(expected, actual);
	}

}
