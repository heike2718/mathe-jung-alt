// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.s2s;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * ClientAuthService
 */
@ApplicationScoped
public class ClientAuthService {

	private static final String AUTH_METHOD_PREFIX = "Basic ";

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAuthService.class);

	@Inject
	MkGatewayAuthConfig authConfig;

	public Pair<String, Boolean> authorize(final String authorizationHeaderValue) {

		LOGGER.debug("expect header={}", StringUtils.abbreviate(authConfig.header(), 11));
		String headerValue = StringUtils.remove(authorizationHeaderValue, AUTH_METHOD_PREFIX);

		try {

			String decodedHeader = new String(Base64.getDecoder().decode(headerValue.getBytes()));
			LOGGER.debug("actual header={}", StringUtils.abbreviate(decodedHeader, 11));
			boolean authenticated = authConfig.header().equals(decodedHeader);
			String clientId = extractClient(decodedHeader);
			return Pair.of(clientId, authenticated);
		} catch (IllegalArgumentException e) {

			LOGGER.debug("Base64-Dekodierung des Authorization-Headers fehlgeschlagen: {}", e.getMessage());
			return Pair.of("", false);
		}
	}

	String extractClient(final String decodedHeader) {

		String[] token = StringUtils.split(decodedHeader, ":");

		if (token.length == 2) {

			return token[0];
		}

		return "client laesst sich nicht ermitteln. Trenner : fehlt oder ist zu oft vorhanden";
	}
}
