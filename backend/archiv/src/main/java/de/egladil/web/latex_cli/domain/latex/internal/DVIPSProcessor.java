// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.domain.latex.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DVIPSProcessor
 */
public class DVIPSProcessor extends AbstractFileProcessor {

	@Override
	protected ProcessBuilder createAndConfigureProcessBuilder(final File file) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("dvips", file.getAbsolutePath());
		processBuilder.directory(file.getParentFile());
		return processBuilder;
	}

	@Override
	protected String getProcessName() {

		return "dvips";
	}

	@Override
	protected File getResultingFile(final File file) {

		return new File(getFilePathWithoutFileExtension(file) + ".ps");
	}

	@Override
	protected int lengthSourceFileExtension() {

		return 4;
	}

	@Override
	protected List<File> getIntermediateFiles(final File file) {

		List<File> result = new ArrayList<>();

		String path = getFilePathWithoutFileExtension(file);
		result.add(new File(path + ".dvi"));

		return result;
	}

}
