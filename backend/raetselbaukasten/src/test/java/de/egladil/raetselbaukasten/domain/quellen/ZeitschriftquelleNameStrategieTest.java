// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.impl.ZeitschriftquelleNameStrategie;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * ZeitschriftquelleNameStrategieTest
 */
@QuarkusTest
public class ZeitschriftquelleNameStrategieTest {

    ZeitschriftquelleNameStrategie strategie = new ZeitschriftquelleNameStrategie();

    QuelleInfosAdapter quelleAdapter = new QuelleInfosAdapter();

    @Test
    void should_getNameThrowIllegalStateException_when_QuellenartNichtZeitschrift() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.PERSON);

        // Act
        try {

            strategie.getText(quelleAdapter.adapt(quelle));
            fail("keine IllegalStateException");
        } catch (IllegalStateException e) {

            assertEquals("Funktioniert nur für Quellenart ZEITSCHRIFT", e.getMessage());
        }

    }

    @Test
    void should_getNameThrowMjaRuntimeException_when_titelBlank() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
        quelle.setMediumTitel(" ");

        // Act
        try {

            strategie.getText(quelleAdapter.adapt(quelle));
            fail("keine MjaRuntimeException");
        } catch (MjaRuntimeException e) {

            assertEquals("Bei Quellenart ZEITSCHRIFT darf mediumTitel nicht blank sein.", e.getMessage());
        }

    }

    @Test
    void should_getName_work_whenOnlyMediumTitelSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
        quelle.setAusgabe(" ");
        quelle.setSeite(null);
        quelle.setJahr("");

        String expected = "Grunschulolympiade 2x2";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

    @Test
    void should_getName_work_whenOnlyMediumTitelUndJahrSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
        quelle.setAusgabe(" ");
        quelle.setSeite(null);

        String expected = "Grunschulolympiade 2x2 1987";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

    @Test
    void should_getName_work_whenOnlyMediumTitelUndJahrUndAusgabeSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
        quelle.setSeite(null);

        String expected = "Grunschulolympiade 2x2 (11) 1987";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

    @Test
    void should_getName_work_whenAllAttributesSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.ZEITSCHRIFT);

        String expected = "Grunschulolympiade 2x2 (11) 1987, S.42";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

}
