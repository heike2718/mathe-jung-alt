// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.infrastructure;

import java.util.concurrent.Callable;

import de.egladil.web.latex_cli.domain.latex.LaTeX2PDFCommand;
import de.egladil.web.latex_cli.domain.latex.LaTeX2PNGCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

/**
 * EntryCommand
 */
@TopCommand
@CommandLine.Command(
	mixinStandardHelpOptions = true, subcommands = { LaTeX2PDFCommand.class, LaTeX2PNGCommand.class })
public class EntryCommand implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {

		return 0;
	}

}
