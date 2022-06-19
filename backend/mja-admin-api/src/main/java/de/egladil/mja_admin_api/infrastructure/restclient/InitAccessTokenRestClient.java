// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.mja_admin_api.infrastructure.auth.dto.OAuthClientCredentials;

/**
 * InitAccessTokenRestClient
 */
@RegisterRestClient
@Path("clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface InitAccessTokenRestClient {

	@POST
	@Path("client/accesstoken")
	Response authenticateClient(OAuthClientCredentials clientSecrets);

}
