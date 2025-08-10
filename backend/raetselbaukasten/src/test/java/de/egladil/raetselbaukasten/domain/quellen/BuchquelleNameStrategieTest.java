// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.impl.BuchquelleNameStrategie;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;

/**
 * BuchquelleNameStrategieTest
 */
@QuarkusTest
public class BuchquelleNameStrategieTest {

	BuchquelleNameStrategie strategie = new BuchquelleNameStrategie();

	QuelleInfosAdapter quelleAdapter = new QuelleInfosAdapter();

	@Test
	void should_getNameThrowIllegalStateException_when_QuellenartNichtBuch() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.setQuellenart(Quellenart.PERSON);

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine IllegalStateException");
		} catch (IllegalStateException e) {

			assertEquals("Funktioniert nur für Quellenart BUCH", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_titelBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.setQuellenart(Quellenart.BUCH);
		quelle.setMediumTitel("  ");

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf mediumTitel nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_autorBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.setQuellenart(Quellenart.BUCH);
		quelle.setAutor("  ");

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf autor nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_seiteBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.setQuellenart(Quellenart.BUCH);
		quelle.setSeite("  ");

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart BUCH darf seite nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameWork() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.setQuellenart(Quellenart.BUCH);

		String expected = "Johannes Lehmann: Grunschulolympiade 2x2, S.42";

		// Act
		String name = strategie.getText(quelleAdapter.adapt(quelle));

		// Assert
		assertEquals(expected, name);
	}

}
