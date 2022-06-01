// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.egladil.mathe_jung_alt_ws.domain.TestFileUtils;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import io.quarkus.test.junit.QuarkusTest;

/**
 * BuchstabenAntwortvorschlagGeneratorStrategegyTest
 */
@QuarkusTest
public class BuchstabenAntwortvorschlagGeneratorStrategegyTest {

	private BuchstabenAntwortvorschlagGeneratorStrategegy strategy = new BuchstabenAntwortvorschlagGeneratorStrategegy();

	@Test
	void should_generateLaTeXAntwortvorschlaegeReturnCenteredTable_when_antwortvorschlaege() throws Exception {

		// Arrange
		Raetsel raetsel = TestFileUtils.loadReaetsel();

		// Act
		String result = strategy.generateLaTeXAntwortvorschlaege(raetsel);

		// Assert
		assertNotNull(result);

		System.out.println(result);

	}

	@Test
	void should_generateLaTeXAntwortvorschlaegeReturnEmptyString_when_AntwortvorschlaegeNull() throws Exception {

		// Arrange
		Raetsel raetsel = new Raetsel("bla");

		// Act
		String result = strategy.generateLaTeXAntwortvorschlaege(raetsel);

		// Assert
		assertTrue(result.isEmpty());

	}

	@Test
	void should_generateLaTeXAntwortvorschlaegeReturnEmptyString_when_noAntwortvorschlaege() throws Exception {

		// Arrange
		Raetsel raetsel = new Raetsel("bla");
		raetsel.withAntwortvorschlaege(new Antwortvorschlag[0]);

		// Act
		String result = strategy.generateLaTeXAntwortvorschlaege(raetsel);

		// Assert
		assertTrue(result.isEmpty());

	}

}
