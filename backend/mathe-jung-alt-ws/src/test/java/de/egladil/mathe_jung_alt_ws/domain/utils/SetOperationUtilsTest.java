// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

/**
 * SetOperationUtilsTest
 */
public class SetOperationUtilsTest {

	@Test
	void should_isLeftSubsetOfRightReturnTrue_when_echteTeilmenge() {

		// Arrange
		String idsSuchanfrage = "1,4,6";
		String idsEntity = "1,2,3,4,5,6,7";

		Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

		// Act
		boolean subset = new SetOperationUtils().isLeftSubsetOfRight(sets);

		// Assert
		assertTrue(subset);

	}

	@Test
	void should_isLeftSubsetOfRightReturnTrue_when_linksLeer() {

		// Arrange
		String idsSuchanfrage = "";
		String idsEntity = "1,2,3,4,5,6,7";

		Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

		// Act
		boolean subset = new SetOperationUtils().isLeftSubsetOfRight(sets);

		// Assert
		assertTrue(subset);

	}

	@Test
	void should_isLeftSubsetOfRightReturnTrue_when_beideLeer() {

		// Arrange
		String idsSuchanfrage = "";
		String idsEntity = "";

		Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

		// Act
		boolean subset = new SetOperationUtils().isLeftSubsetOfRight(sets);

		// Assert
		assertTrue(subset);

	}

	@Test
	void should_isLeftSubsetOfRightReturnFalse_when_rechtsLeer() {

		// Arrange
		String idsSuchanfrage = "1,2,3,4,5,6,7";
		String idsEntity = "";

		Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

		// Act
		boolean subset = new SetOperationUtils().isLeftSubsetOfRight(sets);

		// Assert
		assertFalse(subset);

	}

	@Test
	void should_isLeftSubsetOfRightReturnFalse_when_einElementLinksNichtInRechts() {

		// Arrange
		String idsSuchanfrage = "1,3,5,9";
		String idsEntity = "1,2,3,4,5,6,7";

		Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

		// Act
		boolean subset = new SetOperationUtils().isLeftSubsetOfRight(sets);

		// Assert
		assertFalse(subset);

	}

}
