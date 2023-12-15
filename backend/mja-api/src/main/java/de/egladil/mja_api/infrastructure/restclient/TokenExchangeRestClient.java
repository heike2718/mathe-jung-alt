// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
