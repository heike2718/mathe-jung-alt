// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.embeddable_images;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EmbeddableImagesFilenameDelegateTest
 */
@QuarkusTest
public class EmbeddableImagesFilenameDelegateTest {

    @Inject
    EmbeddableImagesFilenameDelegate delegate;

    @Nested
    class PatternTests {

        @Test
        void should_matchesEpsNameHeikeReturnTrue_when_5digits() {

            assertTrue(delegate.matchesEpsNameHeike("06745.eps"));
        }

        @Test
        void should_matchesEpsNameHeikeReturnFalse_when_4digits() {

            assertFalse(delegate.matchesEpsNameHeike("0675.eps"));
        }

        @Test
        void should_matchesEpsNameHeikeReturnFalse_when_arbitraryName() {

            assertFalse(delegate.matchesEpsNameHeike("apfel.eps"));
        }

    }

    @Nested
    class RelativePathsTests {

        @Test
        void should_getRelativePathForEmbeddableImageReturnTheExpectedPath_when_filenameHeikes() {

            // Arrange
            String filenameEps = "00534.eps";
            String uuid = "4d367e6c-76e9-11ee-975c-325096b39f47";

            String expected = "/resources/4/4d367e6c-00534.eps";

            // Act
            String result = delegate.getRelativePathForEmbeddableImage(uuid, filenameEps);

            // Assert
            assertEquals(expected, result);

        }

        @Test
        void should_getRelativePathForEmbeddableImageReturnTheExpectedPath_when_filenameNotHeikes() {

            // Arrange
            String filenameEps = "apfel.eps";
            String uuid = "4d367e6c-76e9-11ee-975c-325096b39f47";

            String expected = "/resources/4/4d367e6c-76e9.eps";

            // Act
            String result = delegate.getRelativePathForEmbeddableImage(uuid, filenameEps);

            // Assert
            assertEquals(expected, result);

        }

        @Test
        void should_getRelativePathForEmbeddableImageReturnTheExpectedPath_when_filenameCompletelyArbitrary() {

            // Arrange
            String filenameEps = "elefant.jpg";
            String uuid = "4d367e6c-76e9-11ee-975c-325096b39f47";

            String expected = "/resources/4/4d367e6c-76e9.eps";

            // Act
            String result = delegate.getRelativePathForEmbeddableImage(uuid, filenameEps);

            // Assert
            assertEquals(expected, result);

        }

    }

}
