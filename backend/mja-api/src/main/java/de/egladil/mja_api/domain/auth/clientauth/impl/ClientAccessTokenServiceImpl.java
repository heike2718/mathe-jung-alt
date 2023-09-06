// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.clientauth.ClientAccessTokenService;
import de.egladil.mja_api.domain.auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.mja_api.domain.auth.dto.ResponsePayload;
import de.egladil.mja_api.domain.exceptions.ClientAuthException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * ClientAccessTokenServiceImpl
 */
@RequestScoped
public class ClientAccessTokenServiceImpl implements ClientAccessTokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAccessTokenServiceImpl.class);

	@Inject
	OAuthClientCredentialsProvider clientCredentialsProvider;

	@Inject
	InitAccessTokenDelegate initAccessTokenDelegate;

	@Override
	public String orderAccessToken(final String nonce) {

		OAuthClientCredentials credentials = clientCredentialsProvider.getClientCredentials(nonce);

		Response authResponse = null;

		try {

			ResponsePayload responsePayload = initAccessTokenDelegate.authenticateClient(credentials);
			MessagePayload messagePayload = responsePayload.getMessage();

			LOGGER.debug(messagePayload.toString() + " isOK? " + messagePayload.isOk());

			if (!messagePayload.isOk()) {

				LOGGER.error("Authentisierung des Clients hat nicht geklappt: {} - {}", messagePayload.getLevel(),
					messagePayload.getMessage());
				throw new ClientAuthException();
			}

			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) responsePayload.getData();

			String nonceFromResponse = dataMap.get("nonce");

			if (!nonce.equals(nonceFromResponse)) {

				String msg = "Security Thread: zurückgesendetes nonce stimmt nicht: erwarten '" + nonce + "' aktuell: '"
					+ nonceFromResponse + "'";

				LOGGER.warn(msg);
				throw new ClientAuthException();
			}

			String accessToken = dataMap.get("accessToken");

			return accessToken;
		} finally {

			if (authResponse != null) {

				authResponse.close();
			}
		}
	}
}
