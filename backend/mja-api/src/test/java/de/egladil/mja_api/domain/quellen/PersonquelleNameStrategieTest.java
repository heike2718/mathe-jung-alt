// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.impl.PersonquelleNameStrategie;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;

/**
 * PersonquelleNameStrategieTest
 */
@QuarkusTest
public class PersonquelleNameStrategieTest {

	PersonquelleNameStrategie strategie = new PersonquelleNameStrategie();

	QuelleInfosAdapter quelleAdapter = new QuelleInfosAdapter();

	@Test
	void should_getNameThrowIllegalStateException_when_QuellenartNichtPerson() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.BUCH;

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine IllegalStateException");
		} catch (IllegalStateException e) {

			assertEquals("Funktioniert nur für Quellenart PERSON", e.getMessage());
		}

	}

	@Test
	void should_getNameThrowMjaRuntimeException_when_PersonBlank() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.PERSON;
		quelle.person = "  ";

		// Act
		try {

			strategie.getText(quelleAdapter.adapt(quelle));
			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("Bei Quellenart PERSON darf person nicht blank sein.", e.getMessage());
		}

	}

	@Test
	void should_getNameReturnThePerson() {

		// Arrange
		PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
		quelle.quellenart = Quellenart.PERSON;

		// Act
		String name = strategie.getText(quelleAdapter.adapt(quelle));

		// Assert
		assertEquals("Hans Manz", name);

	}

}
