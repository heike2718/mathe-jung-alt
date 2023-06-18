// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;

/**
 * TokenExchangeRestClient
 */
@RegisterRestClient(configKey = "token-api")
@Path("token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TokenExchangeRestClient {

	@PUT
	@Path("exchange/{oneTimeToken}")
	public Response exchangeOneTimeTokenWithJwt(@PathParam(
		value = "oneTimeToken") final String oneTimeToken, final OAuthClientCredentials clientCredentials);

}
