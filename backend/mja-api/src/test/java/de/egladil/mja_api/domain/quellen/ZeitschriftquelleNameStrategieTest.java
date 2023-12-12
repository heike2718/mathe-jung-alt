// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.impl.ZeitschriftquelleNameStrategie;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;

/**
 * ZeitschriftquelleNameStrategieTest
 */
@QuarkusTest
public class ZeitschriftquelleNameStrategieTest {

	ZeitschriftquelleNameStrategie strategie = new ZeitschriftquelleNameStrategie();

	@Test
	void should_getNameThrowIllegalStateException_when_QuellenartNichtZeitschrift() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.PERSON;

		// Act
		try {

			strategie.getName(quelle);
			fail("keine IllegalStateException");
		} catch (IllegalStateException e) {

			assertEquals("Funktioniert nur für Quellenart ZEITSCHRIFT", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_titelBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.ZEITSCHRIFT;
		quelle.mediumTitel = "  ";

		// Act
		try {

			strategie.getName(quelle);
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart ZEITSCHRIFT darf mediumTitel nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getName_work_whenOnlyMediumTitelSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.ZEITSCHRIFT;
		quelle.ausgabe = " ";
		quelle.seite = null;
		quelle.jahr = "";

		String expected = "Grunschulolympiade 2x2";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

	@Test
	void should_getName_work_whenOnlyMediumTitelUndJahrSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.ZEITSCHRIFT;
		quelle.ausgabe = "  ";
		quelle.seite = null;

		String expected = "Grunschulolympiade 2x2 1987";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

	@Test
	void should_getName_work_whenOnlyMediumTitelUndJahrUndAusgabeSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.ZEITSCHRIFT;
		quelle.seite = null;

		String expected = "Grunschulolympiade 2x2 (11) 1987";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

	@Test
	void should_getName_work_whenAllAttributesSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.ZEITSCHRIFT;

		String expected = "Grunschulolympiade 2x2 (11) 1987, S.42";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

}
