// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.restclient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * LaTeXRestClient
 */
@RegisterRestClient(configKey = "latex-api")
@Path("")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public interface LaTeXRestClient {

	/**
	 * Ruft das pdflatex-Command für das gegebene tex-File auf.
	 *
	 * @param  filename
	 *                  String der Name ohne Extension
	 * @return          Response
	 */
	@GET
	@Path("latex2pdf")
	Response latex2PDF(@QueryParam(value = "filename") String filename);

	/**
	 * Ruft das latex2png-Command für das gegebene tex-File auf.
	 *
	 * @param  filename
	 *                  String der Name ohne Extension
	 * @return          Response
	 */
	@GET
	@Path("latex2png")
	Response latex2PNG(@QueryParam(value = "filename") String filename);

}
