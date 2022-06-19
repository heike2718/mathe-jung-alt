// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.login.impl;

import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.dto.OAuthClientCredentials;
import de.egladil.web.mja_auth.dto.ResponsePayload;
import de.egladil.web.mja_auth.exception.ClientAuthException;
import de.egladil.web.mja_auth.exception.InaccessableEndpointException;
import de.egladil.web.mja_auth.exception.MjaAuthRuntimeException;
import de.egladil.web.mja_auth.login.TokenExchangeService;
import de.egladil.web.mja_auth.restclient.TokenExchangeRestClient;

/**
 * TokenExchangeServiceImpl
 */
@RequestScoped
public class TokenExchangeServiceImpl implements TokenExchangeService {

	private static final Logger LOG = LoggerFactory.getLogger(TokenExchangeServiceImpl.class);

	@Inject
	@RestClient
	TokenExchangeRestClient tokenExchangeRestClient;

	@Override
	public String exchangeTheOneTimeToken(final String clientId, final String clientSecret, final String oneTimeToken) {

		final String nonce = UUID.randomUUID().toString();

		OAuthClientCredentials clientCredentials = OAuthClientCredentials.create(clientId, clientSecret, nonce);

		Response response = null;

		try {

			response = tokenExchangeRestClient.exchangeOneTimeTokenWithJwt(oneTimeToken, clientCredentials);

			ResponsePayload responsePayload = response.readEntity(ResponsePayload.class);

			return this.checkNonceAndExtractTheJwt(nonce, responsePayload);

		} catch (WebApplicationException e) {

			ResponsePayload responsePayload = e.getResponse().readEntity(ResponsePayload.class);

			MessagePayload messagePayload = responsePayload.getMessage();

			String message = "Konnte das oneTimeToken nicht gegen das JWT tauschen: " + messagePayload.getMessage();

			LOG.error(message);

			throw new MjaAuthRuntimeException(message);

		} catch (ProcessingException processingException) {

			LOG.error("endpoint authprovider ist nicht erreichbar");

			throw new InaccessableEndpointException("Der Endpoint authprovider ist nicht erreichbar. ");
		} finally {

			if (response != null) {

				response.close();
			}
		}
	}

	private String checkNonceAndExtractTheJwt(final String expectedNonce, final ResponsePayload responsePayload) {

		MessagePayload messagePayload = responsePayload.getMessage();

		if (messagePayload.isOk()) {

			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();
			String responseNonce = dataMap.get("nonce");

			if (!expectedNonce.equals(responseNonce)) {

				{

					LOG.error("Security Thread: zurückgesendetes nonce stimmt nicht");
					throw new ClientAuthException();
				}
			}

			return dataMap.get("jwt");
		} else {

			LOG.error("Authentisierung des Clients hat nicht geklappt: {} - {}", messagePayload.getLevel(),
				messagePayload.getMessage());
			throw new ClientAuthException();
		}
	}

}
