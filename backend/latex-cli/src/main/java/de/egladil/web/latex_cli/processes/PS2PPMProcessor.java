// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * PS2PPMProcessor
 */
public class PS2PPMProcessor extends AbstractFileProcessor {

	@Override
	protected ProcessBuilder createAndConfigureProcessBuilder(final File file) {

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("sh");
		cmdList.add(file.getParent() + File.separator + "ps2ppm.sh");
		cmdList.add(getFileNameWithoutExtension(file));

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(cmdList);
		processBuilder.directory(file.getParentFile());

		return processBuilder;
	}

	@Override
	protected String getProcessName() {

		return "ps2ppm.sh";
	}

	@Override
	protected File getResultingFile(final File file) {

		return new File(getFilePathWithoutFileExtension(file) + ".ppm");
	}

	@Override
	protected int lengthSourceFileExtension() {

		return 3;
	}

	@Override
	protected List<File> getIntermediateFiles(final File file) {

		List<File> result = new ArrayList<>();

		String path = getFilePathWithoutFileExtension(file);
		result.add(new File(path + ".ps"));

		return result;
	}

}
