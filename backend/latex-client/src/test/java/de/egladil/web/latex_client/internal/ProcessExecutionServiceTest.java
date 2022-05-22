// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_client.internal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.egladil.web.latex_client.LaTeXCommand;

/**
 * ProcessExecutionServiceTest
 */
public class ProcessExecutionServiceTest {

	private static final String WORK_DIR = "/home/heike/test/";

	@Test
	void should_run_with_timeout() throws Exception {

		if (!new File(WORK_DIR + "include").isDirectory()) {

			fail("falsche Umgebung: benötige Verzeichnis " + WORK_DIR);
		}

		// Arrange
		prepareTestFiles();

		String shellScript = WORK_DIR + "latex2pdf.sh";
		ProcessExecutionService service = new ProcessExecutionService();
		service.setShellScriptForTest(shellScript);

		// Act
		int exitCode = service.performCommand(LaTeXCommand.PDF, WORK_DIR + "test");

		System.out.println("exitCode=" + exitCode);

		// Assert
		assertTrue(exitCode != 0);
	}

	void prepareTestFiles() throws Exception {

		File shellscript = new File(WORK_DIR + "latex2pdf.sh");
		File testFile = new File(WORK_DIR + "test.tex");

		if (!shellscript.exists() || !shellscript.isFile()) {

			try (InputStream in = getClass().getResourceAsStream("/test/latex2pdf.sh");
				FileOutputStream fos = new FileOutputStream(shellscript)) {

				IOUtils.copy(in, fos);

			}

		}

		if (!testFile.exists() || !testFile.isFile()) {

			try (InputStream in = getClass().getResourceAsStream("/test/test.tex");
				FileOutputStream fos = new FileOutputStream(testFile)) {

				IOUtils.copy(in, fos);

			}

		}

	}

}
