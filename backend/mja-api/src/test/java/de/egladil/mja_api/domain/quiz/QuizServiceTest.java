// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * QuizServiceTest
 */
@QuarkusTest
public class QuizServiceTest {

	QuizService service = new QuizService();

	@Nested
	class BerechneStrafpunkteTests {

		@Test
		void should_return0_when_keineAntwortvorschlaege() {

			// Arrange
			int punkte = 100;
			int anzahlAntwortvorschlaege = 0;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(0, strafpunkte);
		}

		@Test
		void should_return0_when_nurEinAntwortvorschlag() {

			// Arrange
			int punkte = 100;
			int anzahlAntwortvorschlaege = 1;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(0, strafpunkte);
		}

		@Test
		void should_returnPunkte_when_zweiAntwortvorschlaege() {

			// Arrange
			int punkte = 100;
			int anzahlAntwortvorschlaege = 2;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(punkte, strafpunkte);
		}

		@Test
		void should_returnHaelftePunkte_when_dreiAntwortvorschlaege() {

			// Arrange
			int punkte = 300;
			int anzahlAntwortvorschlaege = 3;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(150, strafpunkte);
		}

		@Test
		void should_returnViertelPunkte_when_fuenfAntwortvorschlaege() {

			// Arrange
			int punkte = 500;
			int anzahlAntwortvorschlaege = 5;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(125, strafpunkte);
		}

		@Test
		void should_returnFuenftelPunkte_when_sechsAntwortvorschlaege() {

			// Arrange
			int punkte = 300;
			int anzahlAntwortvorschlaege = 6;

			// Act
			int strafpunkte = service.berechneStrafpunkte(punkte, anzahlAntwortvorschlaege);

			// Assert
			assertEquals(60, strafpunkte);
		}
	}
}
