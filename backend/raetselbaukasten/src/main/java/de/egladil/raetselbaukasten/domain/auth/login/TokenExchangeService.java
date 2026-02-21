// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.login;

import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.dto.OAuthClientCredentials;
import de.egladil.raetselbaukasten.domain.auth.dto.ResponsePayload;
import de.egladil.raetselbaukasten.domain.exceptions.ClientAuthException;
import de.egladil.raetselbaukasten.domain.exceptions.InaccessableEndpointException;
import de.egladil.raetselbaukasten.domain.exceptions.MjaAuthRuntimeException;
import de.egladil.raetselbaukasten.infrastructure.restclient.AuthproviderRestClient;

/**
 * TokenExchangeService
 */
@RequestScoped
public class TokenExchangeService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenExchangeService.class);

    @Inject
    @RestClient
    AuthproviderRestClient authProviderRestClient;

    public String exchangeTheOneTimeToken(final String clientId, final String clientSecret, final String oneTimeToken) {

        final String nonce = UUID.randomUUID().toString();

        OAuthClientCredentials clientCredentials = OAuthClientCredentials.create(clientId, clientSecret, nonce);

        Response response = null;

        try {

            response = authProviderRestClient.exchangeOneTimeTokenWithJwt(oneTimeToken, clientCredentials);

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

            LOG
                    .error("Authentisierung des Clients hat nicht geklappt: {} - {}", messagePayload.getLevel(),
                            messagePayload.getMessage());
            throw new ClientAuthException();
        }
    }

}
