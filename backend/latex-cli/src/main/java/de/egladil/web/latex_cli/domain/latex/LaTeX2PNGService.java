// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.domain.latex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.latex_cli.domain.latex.internal.DVIPSProcessor;
import de.egladil.web.latex_cli.domain.latex.internal.LaTeX2DVIProcessor;
import de.egladil.web.latex_cli.domain.latex.internal.PPM2PNGProcessor;
import de.egladil.web.latex_cli.domain.latex.internal.PS2PPMProcessor;
import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;

/**
 * LaTeX2PNGService
 */
@ApplicationScoped
public class LaTeX2PNGService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeX2PNGService.class);

	@ConfigProperty(name = "path.external.files")
	String pathWorkdir;

	private List<FileProcessor> interceptorChain = new ArrayList<>();

	public static LaTeX2PNGService createForTests() {

		LaTeX2PNGService result = new LaTeX2PNGService();
		result.pathWorkdir = "/home/heike/git/mathe-jung-alt/backend/latexpoc/latex";
		return result;
	}

	/**
	 *
	 */
	public LaTeX2PNGService() {

		interceptorChain.add(new LaTeX2DVIProcessor());
		interceptorChain.add(new DVIPSProcessor());
		interceptorChain.add(new PS2PPMProcessor());
		interceptorChain.add(new PPM2PNGProcessor());

	}

	public Optional<File> transformFile(final String fileName) {

		File file = new File(pathWorkdir + File.separator + fileName);

		for (FileProcessor processor : interceptorChain) {

			try {

				file = processor.transform(file);
			} catch (LaTeXCLIRuntimeException e) {

				LOGGER.error(e.getMessage(), e);

				return Optional.empty();

			}

		}

		return Optional.of(file);

	}

}
