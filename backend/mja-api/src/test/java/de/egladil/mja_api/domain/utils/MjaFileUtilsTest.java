// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * MjaFileUtilsTest
 */
public class MjaFileUtilsTest {

	private static final String PATH_DIR_TO_ZIP = "/home/heike/docker-volumes/latex/doc/testdir-to-zip";

	private static final String PATH_ZIP_FILE = "/home/heike/docker-volumes/latex/doc/testdir-to-zip.zip";

	@Test
	void should_zip_dir_recursively() {

		// Arrange
		File dirToZip = new File(PATH_DIR_TO_ZIP);

		if (!dirToZip.exists() || !dirToZip.exists()) {

			fail("test setup: irgendein Honk hat das Verzeichnis gelöscht.");
		}

		FileUtils.deleteQuietly(new File(PATH_ZIP_FILE));

		// Act
		File zipFile = MjaFileUtils.createZipArchive(new File(PATH_DIR_TO_ZIP));

		// Assert
		assertTrue(zipFile.isFile());
	}
}
