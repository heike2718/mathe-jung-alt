// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.infrastructure.rest;

import java.io.File;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.latex_cli.domain.latex.LaTeX2PNGService;
import de.egladil.web.latex_cli.domain.latex.PDFLaTeXService;
import de.egladil.web.latex_cli.exceptions.InvalidInputException;
import de.egladil.web.latex_cli.infrastructure.FileNameValidator;

/**
 * LaTeXResource
 */
@Path("latex")
public class LaTeXResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXResource.class);

	@Inject
	LaTeX2PNGService laTeXPngService;

	@Inject
	PDFLaTeXService pdfLaTeXService;

	@GET
	@Path("latex2png")
	@Produces(MediaType.TEXT_PLAIN)
	public Response transformToPng(@QueryParam(value = "fileName") final String fileName) {

		try {

			new FileNameValidator().validateFileName("fileName", fileName);
		} catch (InvalidInputException e) {

			LOGGER.warn(e.getMessage() + ": " + e.getInvalidInputs());

			return Response.status(Status.BAD_REQUEST).build();
		}

		Optional<File> optResult = laTeXPngService.transformFile(fileName + ".tex");

		if (optResult.isEmpty()) {

			return Response.serverError().build();
		}

		return Response.ok("File " + optResult.get().getAbsolutePath() + " erzeugt").build();
	}

	@GET
	@Path("pdflatex")
	@Produces(MediaType.TEXT_PLAIN)
	public Response transformToPdf(@QueryParam(value = "fileName") final String fileName) {

		try {

			new FileNameValidator().validateFileName("fileName", fileName);
		} catch (InvalidInputException e) {

			LOGGER.warn(e.getMessage() + ": " + e.getInvalidInputs());

			return Response.status(Status.BAD_REQUEST).build();
		}
		Optional<File> optResult = pdfLaTeXService.transformFile(fileName + ".tex");

		if (optResult.isEmpty()) {

			return Response.serverError().build();
		}

		return Response.ok("File " + optResult.get().getAbsolutePath() + " erzeugt").build();

	}

}
