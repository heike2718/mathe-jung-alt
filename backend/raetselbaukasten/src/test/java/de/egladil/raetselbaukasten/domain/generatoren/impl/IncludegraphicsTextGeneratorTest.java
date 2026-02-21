// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * IncludegraphicsTextGeneratorTest
 */
@QuarkusTest
public class IncludegraphicsTextGeneratorTest {

    IncludegraphicsTextGenerator generator = new IncludegraphicsTextGenerator();

    @Test
    void should_generateIncludegraphicsTextReturnTheExpected() {

        // Arrange
        String expected = "\\begin{center} \\includegraphics[width=0.5\\linewidth]{./resources/4/4d367e6c-00534.eps} \\end{center}";
        String relativePath = "/resources/4/4d367e6c-00534.eps";

        // Act
        String actual = generator.generateIncludegraphicsText(relativePath);

        // Assert
        assertEquals(expected, actual);

        System.out.println(actual);

    }

}
