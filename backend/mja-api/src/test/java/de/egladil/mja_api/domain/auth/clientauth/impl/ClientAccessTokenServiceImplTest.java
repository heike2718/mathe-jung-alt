// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.ClientType;
import de.egladil.mja_api.domain.auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.mja_api.domain.auth.dto.ResponsePayload;
import de.egladil.mja_api.domain.exceptions.ClientAuthException;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * ClientAccessTokenServiceImplTest
 */
@QuarkusTest
public class ClientAccessTokenServiceImplTest {

	@Inject
	ClientAccessTokenServiceImpl service;

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

		when(clientCredentialsProvider.getClientCredentials(ClientType.ADMIN, nonce)).thenReturn(credentials);
		when(initAccessTokenDelegate.authenticateClient(credentials)).thenReturn(responsePayload);

		// Act
		String result = service.orderAccessToken(ClientType.ADMIN, nonce);

		// Assert
		assertEquals(accessToken, result);
		verify(clientCredentialsProvider).getClientCredentials(ClientType.ADMIN, nonce);
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

		when(clientCredentialsProvider.getClientCredentials(ClientType.ADMIN, nonce)).thenReturn(credentials);
		when(initAccessTokenDelegate.authenticateClient(credentials)).thenReturn(responsePayload);

		try {

			service.orderAccessToken(ClientType.ADMIN, nonce);

			fail("keine ClientAuthException");
		} catch (ClientAuthException e) {

			verify(clientCredentialsProvider).getClientCredentials(ClientType.ADMIN, nonce);
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

		when(clientCredentialsProvider.getClientCredentials(ClientType.ADMIN, nonce)).thenReturn(credentials);
		when(initAccessTokenDelegate.authenticateClient(credentials))
			.thenThrow(new MjaRuntimeException("schlimm, schlimm, schlimm"));

		try {

			service.orderAccessToken(ClientType.ADMIN, nonce);

			fail("keine MjaRuntimeException");
		} catch (MjaRuntimeException e) {

			assertEquals("schlimm, schlimm, schlimm", e.getMessage());

			verify(clientCredentialsProvider).getClientCredentials(ClientType.ADMIN, nonce);
			verify(initAccessTokenDelegate).authenticateClient(credentials);

		}
	}

}
