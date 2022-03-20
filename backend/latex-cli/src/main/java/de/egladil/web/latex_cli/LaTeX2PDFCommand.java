// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.File;
import java.util.concurrent.Callable;

import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;
import de.egladil.web.latex_cli.processes.LaTeX2PDFProcessor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * LaTeX2PDFCommand
 */
@Command(
	name = "jpdflatex", mixinStandardHelpOptions = true, version = "jpdflatex 0.0.1",
	description = "transforms a given tex-File to pdf")
public class LaTeX2PDFCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The tex-file that is going to be transformed")
	private File file;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeX2PDFCommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		try {

			new LaTeX2PDFProcessor().transform(file);
			return 0;
		} catch (LaTeXCLIRuntimeException e) {

			return e.getExitCode();

		}
	}

}
