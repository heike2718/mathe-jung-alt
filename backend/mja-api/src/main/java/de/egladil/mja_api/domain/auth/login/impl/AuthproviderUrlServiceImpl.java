// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.clientauth.ClientAccessTokenService;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.login.AuthproviderUrlService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderUrlServiceImpl
 */
@RequestScoped
public class AuthproviderUrlServiceImpl implements AuthproviderUrlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthproviderUrlServiceImpl.class);

	@ConfigProperty(name = "auth-app.url")
	String authAppUrl;

	@ConfigProperty(name = "public-redirect-url")
	String publicRedirectUrl;

	@Inject
	ClientAccessTokenService clientAccessTokenService;

	@Override
	public Response getLoginUrl() {

		// hierher ausgelagert, damit ClientAccessTokenService testbar wird.
		String nonce = UUID.randomUUID().toString();
		String accessToken = clientAccessTokenService.orderAccessToken(nonce);

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String redirectUrl = authAppUrl + "#/login?accessToken=" + accessToken + "&state=login&redirectUrl="
			+ publicRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(MessagePayload.info(redirectUrl)).build();
	}

	@Override
	public Response getSignupUrl() {

		// hierher ausgelagert, damit ClientAccessTokenService testbar wird.
		String nonce = UUID.randomUUID().toString();
		String accessToken = clientAccessTokenService.orderAccessToken(nonce);

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String redirectUrl = authAppUrl + "#/signup?accessToken=" + accessToken + "&state=signup&redirectUrl="
			+ publicRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(MessagePayload.info(redirectUrl)).build();
	}
}
