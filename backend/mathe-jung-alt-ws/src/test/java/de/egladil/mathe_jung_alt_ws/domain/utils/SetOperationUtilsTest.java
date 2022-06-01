// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * SetOperationUtilsTest
 */
@QuarkusTest
public class SetOperationUtilsTest {

	private SetOperationUtils setOperationUtils = new SetOperationUtils();

	@Nested
	class PrepareDeskriptorenTests {

		@Test
		void should_prepareForDeskriptorenLikeSearchReturnNull_when_parameterNull() {

			assertNull(setOperationUtils.prepareForDeskriptorenLikeSearch(null));

		}

		@Test
		void should_prepareForDeskriptorenLikeSearchReturnNull_when_parameterBlank() {

			assertNull(setOperationUtils.prepareForDeskriptorenLikeSearch(" "));

		}

		@Test
		void should_prepareForDeskriptorenLikeSearchReturnSortetString_when_parameterNotNull() {

			// Arrange
			String deskriptorenIDs = "43,7,9,3,15";
			String expected = "%3%,%7%,%9%,%15%,%43%";

			// Act
			String result = setOperationUtils.prepareForDeskriptorenLikeSearch(deskriptorenIDs);

			// Assert
			assertEquals(expected, result);

		}

		@Test
		void should_prepareForDeskriptorenLikeSearchReturnSortetStringWithoutDuplicates_when_parameterNotNullContainsDuplicates() {

			// Arrange
			String deskriptorenIDs = "43,3,7,9,3,15";
			String expected = "%3%,%7%,%9%,%15%,%43%";

			// Act
			String result = setOperationUtils.prepareForDeskriptorenLikeSearch(deskriptorenIDs);

			// Assert
			assertEquals(expected, result);

		}

	}

	@Nested
	class SubsetTests {

		@Test
		void should_isLeftSubsetOfRightReturnTrue_when_echteTeilmenge() {

			// Arrange
			String idsSuchanfrage = "1,4,6";
			String idsEntity = "1,2,3,4,5,6,7";

			Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

			// Act
			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

			// Assert
			assertTrue(subset);

		}

		@Test
		void should_isLeftSubsetOfRightReturnTrue_when_echteTeilmengeZusammengedampftAufMenge() {

			// Arrange
			String idsSuchanfrage = "1,1,4,4,6";
			String idsEntity = "1,2,3,4,5,6,7";

			Pair<String, String> sets = Pair.of(idsSuchanfrage, idsEntity);

			// Act
			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

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

			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

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
			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

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
			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

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
			boolean subset = setOperationUtils.isLeftSubsetOfRight(sets);

			// Assert
			assertFalse(subset);

		}
	}

}
