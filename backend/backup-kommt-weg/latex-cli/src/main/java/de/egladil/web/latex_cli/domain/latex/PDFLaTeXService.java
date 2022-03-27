// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.domain.latex;

import java.io.File;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.latex_cli.domain.latex.internal.LaTeX2PDFProcessor;
import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;

/**
 * PDFLaTeXService
 */
@ApplicationScoped
public class PDFLaTeXService {

	public static final Logger LOGGER = LoggerFactory.getLogger(PDFLaTeXService.class);

	@ConfigProperty(name = "path.external.files")
	String pathWorkdir;

	private final LaTeX2PDFProcessor processor = new LaTeX2PDFProcessor();

	public static PDFLaTeXService createForTests() {

		PDFLaTeXService result = new PDFLaTeXService();
		result.pathWorkdir = "/home/heike/git/mathe-jung-alt/backend/testfiles";
		return result;
	}

	public Optional<File> transformFile(final String fileName) {

		File file = new File(pathWorkdir + File.separator + fileName);

		try {

			file = processor.transform(file);
		} catch (LaTeXCLIRuntimeException e) {

			LOGGER.error(e.getMessage(), e);

			return Optional.empty();

		}

		return Optional.of(file);
	}
}
