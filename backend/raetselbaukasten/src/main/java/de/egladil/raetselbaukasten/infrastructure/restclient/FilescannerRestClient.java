// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.raetselbaukasten.domain.upload.ScanRequestPayload;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * FilescannerRestClient
 */
@RegisterRestClient(configKey = "filescannerrestclient")
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FilescannerRestClient {

	@POST
	@Path("files/detection/v1")
	Response scanUpload(ScanRequestPayload payload);

	@GET
	@Path("files/ping/v1")
	Response ping(@HeaderParam(value = "X-CLIENT-ID") String clientId);
}
