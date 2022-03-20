// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * PPM2PNGProcessor
 */
public class PPM2PNGProcessor extends AbstractFileProcessor {

	@Override
	protected ProcessBuilder createAndConfigureProcessBuilder(final File file) {

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("sh");
		cmdList.add(file.getParent() + File.separator + "ppm2png.sh");
		cmdList.add(getFileNameWithoutExtension(file));

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(cmdList);
		processBuilder.directory(file.getParentFile());

		return processBuilder;
	}

	@Override
	protected String getProcessName() {

		return "ppm2png.sh";
	}

	@Override
	protected File getResultingFile(final File file) {

		return new File(getFilePathWithoutFileExtension(file) + ".png");
	}

	@Override
	protected int lengthSourceFileExtension() {

		return 4;
	}

	@Override
	protected List<File> getIntermediateFiles(final File file) {

		List<File> result = new ArrayList<>();

		String path = getFilePathWithoutFileExtension(file);
		result.add(new File(path + ".ppm"));

		return result;
	}

}
