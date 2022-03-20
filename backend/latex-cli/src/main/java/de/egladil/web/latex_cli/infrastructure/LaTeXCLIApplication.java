// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.infrastructure;

import javax.inject.Inject;

import org.apache.maven.shared.utils.cli.CommandLineUtils;

import de.egladil.web.latex_cli.domain.latex.LaTeX2PDFCommand;
import de.egladil.web.latex_cli.domain.latex.LaTeX2PNGCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;

/**
 * LaTeXCLIApplication
 */
@QuarkusMain
public class LaTeXCLIApplication implements QuarkusApplication {

	@Inject
	LaTeX2PNGCommand laTeX2PNGCommand;

	@Inject
	LaTeX2PDFCommand laTeX2PDFCommand;

	@Override
	public int run(final String... args) throws Exception {

		if (args.length == 0) {

			Quarkus.waitForExit();
			return 0;
		}

		String[] parsedArgs = new String[0];

		if (args.length == 1) {

			parsedArgs = CommandLineUtils.translateCommandline(args[0]);
		}
		new CommandLine(new EntryCommand())
			.addSubcommand(laTeX2PDFCommand)
			.addSubcommand(laTeX2PNGCommand)
			.execute(parsedArgs);

		return 0;
	}

	public static void main(final String[] args) {

		Quarkus.run(LaTeXCLIApplication.class, args);
	}

}
