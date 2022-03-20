// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.domain.latex;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * LaTeX2PDFCommand
 */
@Dependent
@Command(
	name = "jpdflatex", mixinStandardHelpOptions = true, version = "jpdflatex 0.0.1",
	description = "transforms a given tex-File to pdf")
public class LaTeX2PDFCommand implements Callable<Integer> {

	@Parameters(
		index = "0",
		description = "The name of the tex-file that is going to be transformed. Location is configured in application.properties")
	private String fileName;

	@Inject
	PDFLaTeXService service;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeX2PDFCommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		if (service == null) {

			service = PDFLaTeXService.createForTests();
		}

		Optional<File> optFile = service.transformFile(fileName);

		return optFile.isEmpty() ? 1 : 0;
	}

}
