// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import de.egladil.raetselbaukasten.TestFileUtils;
import de.egladil.raetselbaukasten.domain.raetsel.Antwortvorschlag;
import de.egladil.raetselbaukasten.domain.raetsel.Raetsel;
import de.egladil.raetselbaukasten.profiles.FullDatabaseAdminTestProfile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DescriptionAntwortvorschlagGeneratorStrategegyTest
 */
@QuarkusTest
@TestProfile(FullDatabaseAdminTestProfile.class)
public class DescriptionAntwortvorschlagGeneratorStrategegyTest {

    private DescriptionAntwortvorschlagGeneratorStrategegy strategy = new DescriptionAntwortvorschlagGeneratorStrategegy();

    @Test
    void should_generateLaTeXAntwortvorschlaegeReturnCenteredTable_when_antwortvorschlaege() throws Exception {

        // Arrange
        Raetsel raetsel = TestFileUtils.loadReaetsel();

        // Act
        String result = strategy.generateLaTeXAntwortvorschlaege(raetsel.getAntwortvorschlaege());

        // Assert
        assertNotNull(result);

        System.out.println(result);

    }

    @Test
    void should_generateLaTeXAntwortvorschlaegeReturnEmptyString_when_AntwortvorschlaegeNull() throws Exception {

        // Arrange
        Raetsel raetsel = new Raetsel("bla");

        // Act
        String result = strategy.generateLaTeXAntwortvorschlaege(raetsel.getAntwortvorschlaege());

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void should_generateLaTeXAntwortvorschlaegeReturnEmptyString_when_noAntwortvorschlaege() throws Exception {

        // Arrange
        Raetsel raetsel = new Raetsel("bla");
        raetsel.withAntwortvorschlaege(new Antwortvorschlag[0]);

        // Act
        String result = strategy.generateLaTeXAntwortvorschlaege(raetsel.getAntwortvorschlaege());

        // Assert
        assertTrue(result.isEmpty());

    }

}
