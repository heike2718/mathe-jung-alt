// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.clientauth.impl;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import de.egladil.raetselbaukasten.domain.auth.clientauth.ClientAccessTokenService;
import de.egladil.raetselbaukasten.domain.auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.dto.OAuthClientCredentials;
import de.egladil.raetselbaukasten.domain.auth.dto.ResponsePayload;
import de.egladil.raetselbaukasten.domain.exceptions.ClientAuthException;
import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ClientAccessTokenServiceImplTest
 */
@QuarkusTest
public class ClientAccessTokenServiceImplTest {

    @Inject
    ClientAccessTokenService service;

    @InjectMock
    OAuthClientCredentialsProvider clientCredentialsProvider;

    @InjectMock
    InitAccessTokenDelegate initAccessTokenDelegate;

    @Test
    void should_orderAccessTokenWork() {

        // Arrange
        String nonce = "dqhohod";
        String accessToken = "12345";

        OAuthClientCredentials credentials = new OAuthClientCredentials();
        credentials.setClientId("sodhowho");
        credentials.setClientSecret("shdfhqho");
        credentials.setNonce(nonce);

        Map<String, String> data = new HashMap<>();
        data.put("nonce", nonce);
        data.put("accessToken", accessToken);

        ResponsePayload responsePayload = ResponsePayload.messageOnly(MessagePayload.ok());
        responsePayload.setData(data);

        when(clientCredentialsProvider.getClientCredentials(nonce)).thenReturn(credentials);
        when(initAccessTokenDelegate.authenticateClient(credentials)).thenReturn(responsePayload);

        // Act
        String result = service.orderAccessToken(nonce);

        // Assert
        assertEquals(accessToken, result);
        verify(clientCredentialsProvider).getClientCredentials(nonce);
        verify(initAccessTokenDelegate).authenticateClient(credentials);
    }

    @Test
    void should_orderAccessTokenThrowMClientAuthException_when_nonceTampered() {

        // Arrange
        String nonce = "dqhohod";
        String accessToken = "12345";

        OAuthClientCredentials credentials = new OAuthClientCredentials();
        credentials.setClientId("sodhowho");
        credentials.setClientSecret("shdfhqho");
        credentials.setNonce(nonce);

        Map<String, String> data = new HashMap<>();
        data.put("nonce", "hawhodq");
        data.put("accessToken", accessToken);

        ResponsePayload responsePayload = ResponsePayload.messageOnly(MessagePayload.ok());
        responsePayload.setData(data);

        when(clientCredentialsProvider.getClientCredentials(nonce)).thenReturn(credentials);
        when(initAccessTokenDelegate.authenticateClient(credentials)).thenReturn(responsePayload);

        try {

            service.orderAccessToken(nonce);

            fail("keine ClientAuthException");
        } catch (ClientAuthException e) {

            verify(clientCredentialsProvider).getClientCredentials(nonce);
            verify(initAccessTokenDelegate).authenticateClient(credentials);

        }
    }

    @Test
    void should_orderAccessTokenPropagateMjaExcetion() {

        // Arrange
        String nonce = "dqhohod";
        String accessToken = "12345";

        OAuthClientCredentials credentials = new OAuthClientCredentials();
        credentials.setClientId("sodhowho");
        credentials.setClientSecret("shdfhqho");
        credentials.setNonce(nonce);

        Map<String, String> data = new HashMap<>();
        data.put("nonce", "hawhodq");
        data.put("accessToken", accessToken);

        ResponsePayload responsePayload = ResponsePayload.messageOnly(MessagePayload.ok());
        responsePayload.setData(data);

        when(clientCredentialsProvider.getClientCredentials(nonce)).thenReturn(credentials);
        when(initAccessTokenDelegate.authenticateClient(credentials))
                .thenThrow(new MjaRuntimeException("schlimm, schlimm, schlimm"));

        try {

            service.orderAccessToken(nonce);

            fail("keine MjaRuntimeException");
        } catch (MjaRuntimeException e) {

            assertEquals("schlimm, schlimm, schlimm", e.getMessage());

            verify(clientCredentialsProvider).getClientCredentials(nonce);
            verify(initAccessTokenDelegate).authenticateClient(credentials);

        }
    }

}
