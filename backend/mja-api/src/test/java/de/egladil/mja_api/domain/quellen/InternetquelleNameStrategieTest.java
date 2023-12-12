// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.impl.InternetquelleNameStrategie;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;

/**
 * InternetquelleNameStrategieTest
 */
@QuarkusTest
public class InternetquelleNameStrategieTest {

	InternetquelleNameStrategie strategie = new InternetquelleNameStrategie();

	@Test
	void should_getNameThrowIllegalStateException_when_QuellenartNichtPerson() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;

		// Act
		try {

			strategie.getName(quelle);
			fail("keine IllegalStateException");
		} catch (IllegalStateException e) {

			assertEquals("Funktioniert nur für Quellenart INTERNET", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_titelBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.INTERNET;
		quelle.mediumTitel = "  ";

		// Act
		try {

			strategie.getName(quelle);
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart INTERNET darf mediumTitel nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getName_work_whenOnlyMediumTitelSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.INTERNET;
		quelle.jahr = "";
		quelle.klasse = "  ";
		quelle.stufe = null;

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
		quelle.quellenart = Quellenart.INTERNET;
		quelle.klasse = "  ";
		quelle.stufe = null;

		String expected = "Grunschulolympiade 2x2 (1987)";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

	@Test
	void should_getName_work_whenOnlyMediumTitelUndJahrUndKlasseSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.INTERNET;
		quelle.stufe = null;

		String expected = "Grunschulolympiade 2x2 (1987), Klasse 4";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

	@Test
	void should_getName_work_whenAllAttributesSet() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.INTERNET;

		String expected = "Grunschulolympiade 2x2 (1987), Klasse 4, Stufe 2";

		// Act
		String name = strategie.getName(quelle);

		// Assert
		assertEquals(expected, name);

	}

}
