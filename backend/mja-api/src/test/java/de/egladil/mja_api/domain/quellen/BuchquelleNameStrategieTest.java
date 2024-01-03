// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.impl.BuchquelleNameStrategie;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;

/**
 * BuchquelleNameStrategieTest
 */
@QuarkusTest
public class BuchquelleNameStrategieTest {

	BuchquelleNameStrategie strategie = new BuchquelleNameStrategie();

	@Test
	void should_getNameThrowIllegalStateException_when_QuellenartNichtBuch() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.PERSON;

		// Act
		try {

			strategie.getText(quelle);
			fail("keine IllegalStateException");
		} catch (IllegalStateException e) {

			assertEquals("Funktioniert nur für Quellenart BUCH", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_titelBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;
		quelle.mediumTitel = "  ";

		// Act
		try {

			strategie.getText(quelle);
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf mediumTitel nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_autorBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;
		quelle.autor = "  ";

		// Act
		try {

			strategie.getText(quelle);
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf autor nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_seiteBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;
		quelle.seite = "  ";

		// Act
		try {

			strategie.getText(quelle);
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf seite nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameWork() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;

		String expected = "Johannes Lehmann: Grunschulolympiade 2x2, S.42";

		// Act
		String name = strategie.getText(quelle);

		// Assert
		assertEquals(expected, name);
	}

}
