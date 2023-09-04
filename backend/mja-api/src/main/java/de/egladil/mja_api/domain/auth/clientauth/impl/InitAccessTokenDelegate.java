// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth.impl;

import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.mja_api.domain.auth.dto.ResponsePayload;
import de.egladil.mja_api.domain.exceptions.MjaAuthRuntimeException;
import de.egladil.mja_api.infrastructure.restclient.InitAccessTokenRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * InitAccessTokenDelegate kapselt den Aufruf des RestClients.
 */
@ApplicationScoped
public class InitAccessTokenDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitAccessTokenDelegate.class);

	@Inject
	@RestClient
	InitAccessTokenRestClient initAccessTokenRestClient;

	/**
	 * @param  clientSecrets
	 * @return
	 */
	public ResponsePayload authenticateClient(final OAuthClientCredentials credentials) {

		Response authResponse = null;

		try {

			authResponse = initAccessTokenRestClient.authenticateClient(credentials);

			ResponsePayload responsePayload = authResponse.readEntity(ResponsePayload.class);

			return responsePayload;
		} catch (IllegalStateException | RestClientDefinitionException | WebApplicationException e) {

			String msg = "Unerwarteter Fehler beim Anfordern eines client-accessTokens: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaAuthRuntimeException(msg, e);
		} finally {

			if (authResponse != null) {

				authResponse.close();
			}
		}
	}
}
