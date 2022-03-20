// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;
import de.egladil.web.latex_cli.processes.DVIPSProcessor;
import de.egladil.web.latex_cli.processes.LaTeX2DVIProcessor;
import de.egladil.web.latex_cli.processes.PPM2PNGProcessor;
import de.egladil.web.latex_cli.processes.PS2PPMProcessor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * LaTeX2PNGCommand
 */
@Command(
	name = "jlatex2png", mixinStandardHelpOptions = true, version = "jlatex2png 0.0.1",
	description = "transforms a given tex-File to png")
public class LaTeX2PNGCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The tex-file that is going to be transformed")
	private File file;

	private List<FileProcessor> interceptorChain = new ArrayList<>();;

	/**
	 *
	 */
	public LaTeX2PNGCommand() {

		interceptorChain.add(new LaTeX2DVIProcessor());
		interceptorChain.add(new DVIPSProcessor());
		interceptorChain.add(new PS2PPMProcessor());
		interceptorChain.add(new PPM2PNGProcessor());

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeX2PNGCommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		File source = new File(this.file.getAbsolutePath());

		for (FileProcessor processor : interceptorChain) {

			try {

				source = processor.transform(source);
			} catch (LaTeXCLIRuntimeException e) {

				return e.getExitCode();

			}

		}

		return 0;
	}

}
