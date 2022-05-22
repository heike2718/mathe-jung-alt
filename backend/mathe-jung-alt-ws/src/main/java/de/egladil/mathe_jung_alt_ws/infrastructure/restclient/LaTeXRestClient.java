// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * LaTeXRestClient
 */
@RegisterRestClient
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
