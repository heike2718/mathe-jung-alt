// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;

import de.egladil.web.latex_service.LaTeXCommand;

/**
 * ProcessExecutionService
 */
public class ProcessExecutionService {

	private static final Logger LOGGER = Logger.getLogger(ProcessExecutionService.class);

	private String shellScript;

	public int performCommand(final LaTeXCommand cmd, final String fileName) {

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("sh");

		if (this.shellScript == null) {

			cmdList.add(cmd.getShellScript());
		} else {

			cmdList.add(this.shellScript);
		}
		cmdList.add(fileName);

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
		processBuilder.command(cmdList);
		processBuilder.inheritIO();

		Process process = null;

		try {

			process = processBuilder.start();
			LOGGER.debug("process started");
		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			return -1;
		}

		try {

			final boolean exited = process.waitFor(20, TimeUnit.SECONDS);

			if (!exited) {

				LOGGER.error("process did not finish within 10 seconds");
			}
		} catch (final InterruptedException e) {

			LOGGER.error(e.getMessage(), e);

			return -2;
		}

		try {

			int exitCode = process.exitValue();

			if (exitCode != 0) {

				LOGGER.error(cmd.getShellScript() + " " + fileName + " exited with exitCode code " + exitCode);
				return exitCode;
			}

			return 0;
		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);

			return -3;
		}

	}

	void setShellScriptForTest(final String shellScriptPath) {

		this.shellScript = shellScriptPath;
	}

}
