// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.clientauth.impl;

import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.ClientType;
import de.egladil.web.mja_auth.clientauth.ClientAccessTokenService;
import de.egladil.web.mja_auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.dto.OAuthClientCredentials;
import de.egladil.web.mja_auth.dto.ResponsePayload;
import de.egladil.web.mja_auth.exception.ClientAuthException;
import de.egladil.web.mja_auth.exception.MjaAuthRuntimeException;
import de.egladil.web.mja_auth.restclient.InitAccessTokenRestClient;

/**
 * ClientAccessTokenServiceImpl
 */
@RequestScoped
public class ClientAccessTokenServiceImpl implements ClientAccessTokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAccessTokenServiceImpl.class);

	@Inject
	OAuthClientCredentialsProvider clientCredentialsProvider;

	@Inject
	@RestClient
	InitAccessTokenRestClient initAccessTokenRestClient;

	@Override
	public String orderAccessToken(final ClientType clientType) {

		String nonce = UUID.randomUUID().toString();
		OAuthClientCredentials credentials = clientCredentialsProvider.getClientCredentials(clientType, nonce);

		Response authResponse = null;

		try {

			authResponse = initAccessTokenRestClient.authenticateClient(credentials);

			ResponsePayload responsePayload = authResponse.readEntity(ResponsePayload.class);

			evaluateResponse(nonce, responsePayload);

			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String accessToken = dataMap.get("accessToken");

			return accessToken;
		} catch (IllegalStateException | RestClientDefinitionException | WebApplicationException e) {

			String msg = "Unerwarteter Fehler beim Anfordern eines client-accessTokens: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaAuthRuntimeException(msg, e);
		} catch (ClientAuthException e) {

			// wurde schon geloggt
			return null;
		} finally {

			if (authResponse != null) {

				authResponse.close();
			}
		}
	}

	private void evaluateResponse(final String nonce, final ResponsePayload responsePayload) throws ClientAuthException {

		MessagePayload messagePayload = responsePayload.getMessage();

		if (messagePayload.isOk()) {

			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String responseNonce = dataMap.get("nonce");

			if (!nonce.equals(responseNonce)) {

				String msg = "Security Thread: zurückgesendetes nonce stimmt nicht";

				LOGGER.warn(msg);
				throw new ClientAuthException();
			}
		} else {

			LOGGER.error("Authentisierung des Clients hat nicht geklappt: {} - {}", messagePayload.getLevel(),
				messagePayload.getMessage());
			throw new ClientAuthException();
		}
	}
}
