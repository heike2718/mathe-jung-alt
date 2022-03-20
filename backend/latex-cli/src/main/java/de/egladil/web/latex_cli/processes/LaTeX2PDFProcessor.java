// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * LaTeX2PDFProcessor
 */
public class LaTeX2PDFProcessor extends AbstractFileProcessor {

	@Override
	protected ProcessBuilder createAndConfigureProcessBuilder(final File file) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("pdflatex", file.getAbsolutePath());
		processBuilder.directory(file.getParentFile());
		return processBuilder;
	}

	@Override
	protected String getProcessName() {

		return "pdflatex";
	}

	@Override
	protected File getResultingFile(final File file) {

		return new File(getFilePathWithoutFileExtension(file) + ".pdf");
	}

	@Override
	protected int lengthSourceFileExtension() {

		return 4;
	}

	@Override
	protected List<File> getIntermediateFiles(final File file) {

		List<File> result = new ArrayList<>();

		String path = getFilePathWithoutFileExtension(file);

		result.add(new File(path + ".aux"));
		result.add(new File(path + ".log"));
		result.add(new File(path + ".out"));

		return result;
	}

}
