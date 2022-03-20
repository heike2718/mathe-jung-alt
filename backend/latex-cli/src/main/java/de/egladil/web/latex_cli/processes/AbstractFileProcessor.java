// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.processes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.latex_cli.FileProcessor;
import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;

/**
 * AbstractFileProcessor
 */
public abstract class AbstractFileProcessor implements FileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileProcessor.class);

	@Override
	public File transform(final File file) throws LaTeXCLIRuntimeException {

		LOGGER.debug("Start processing {}", file.getAbsolutePath());

		try {

			ProcessBuilder processBuilder = createAndConfigureProcessBuilder(file);

			Process process = processBuilder.start();
			LOGGER.debug("process started");

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();

			while ((line = br.readLine()) != null) {

				sb.append(line);
			}

			LOGGER.debug(sb.toString());

			int exitCode = process.waitFor();

			LOGGER.info("{} exited with exitCode code {}", getProcessName(), exitCode);

			if (exitCode != 0) {

				throw new LaTeXCLIRuntimeException(exitCode, "Fehler beim Process " + getProcessName() + ": " + sb.toString());
			}

			return getResultingFile(file);

		} catch (IOException e) {

			throw new LaTeXCLIRuntimeException(-1, e.getMessage(), e);

		} catch (InterruptedException e) {

			throw new LaTeXCLIRuntimeException(-2, e.getMessage(), e);
		} finally {

			removeIntermediateFiles(file);

		}
	}

	/**
	 *
	 */
	private void removeIntermediateFiles(final File file) {

		List<File> intermediateFiles = getIntermediateFiles(file);

		for (File f : intermediateFiles) {

			if (f.isFile()) {

				try {

					f.delete();
				} catch (Exception e) {

					LOGGER.warn("intermediate file {} konnte nicht geloescht werden", f.getAbsolutePath());

				}
			}

		}

	}

	protected abstract ProcessBuilder createAndConfigureProcessBuilder(File file);

	protected abstract String getProcessName();

	protected abstract File getResultingFile(File file);

	protected List<File> getIntermediateFiles(final File file) {

		return new ArrayList<>();
	}

	protected String getFilePathWithoutFileExtension(final File file) {

		String filePathWithoutFileExtension = file.getParent() + File.separator + getFileNameWithoutExtension(file);
		return filePathWithoutFileExtension;
	}

	protected String getFileNameWithoutExtension(final File file) {

		String name = file.getName();
		String fileNameWithoutExtension = name.substring(0, name.length() - lengthSourceFileExtension());
		return fileNameWithoutExtension;

	}

	/**
	 * @return
	 */
	protected abstract int lengthSourceFileExtension();

}
