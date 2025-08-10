// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.impl.InternetquelleNameStrategie;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * InternetquelleNameStrategieTest
 */
@QuarkusTest
public class InternetquelleNameStrategieTest {

    InternetquelleNameStrategie strategie = new InternetquelleNameStrategie();

    QuelleInfosAdapter quelleAdapter = new QuelleInfosAdapter();

    @Test
    void should_getNameThrowIllegalStateException_when_QuellenartNichtPerson() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.BUCH);

        // Act
        try {

            strategie.getText(quelleAdapter.adapt(quelle));
            fail("keine IllegalStateException");
        } catch (IllegalStateException e) {

            assertEquals("Funktioniert nur für Quellenart INTERNET", e.getMessage());
        }

    }

    @Test
    void should_getNameThrowMjaRuntimeException_when_titelBlank() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.INTERNET);
        quelle.setMediumTitel("  ");

        // Act
        try {

            strategie.getText(quelleAdapter.adapt(quelle));
            fail("keine MjaRuntimeException");
        } catch (MjaRuntimeException e) {

            assertEquals("Bei Quellenart INTERNET darf mediumTitel nicht blank sein.", e.getMessage());
        }

    }

    @Test
    void should_getName_work_whenOnlyMediumTitelSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.INTERNET);
        quelle.setJahr("");
        quelle.setKlasse(" ");
        quelle.setStufe(null);

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
        quelle.setQuellenart(Quellenart.INTERNET);
        quelle.setKlasse(" ");
        quelle.setStufe(null);

        String expected = "Grunschulolympiade 2x2 (1987)";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

    @Test
    void should_getName_work_whenOnlyMediumTitelUndJahrUndKlasseSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.INTERNET);
        quelle.setStufe(null);

        String expected = "Grunschulolympiade 2x2 (1987), Klasse 4";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

    @Test
    void should_getName_work_whenAllAttributesSet() {

        // Arrange
        PersistenteQuelleReadonly quelle = QuellenNameTestUtils.createQuelleAlleAttributeOhneQuellenart();
        quelle.setQuellenart(Quellenart.INTERNET);

        String expected = "Grunschulolympiade 2x2 (1987), Klasse 4, Stufe 2";

        // Act
        String name = strategie.getText(quelleAdapter.adapt(quelle));

        // Assert
        assertEquals(expected, name);

    }

}
