// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.login;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.auth.clientauth.ClientAccessTokenService;
import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * AuthproviderUrlService
 */
@RequestScoped
public class AuthproviderUrlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthproviderUrlService.class);

	@ConfigProperty(name = "auth-app.url")
	String authAppUrl;

	@ConfigProperty(name = "public-redirect-url")
	String publicRedirectUrl;

	@Inject
	ClientAccessTokenService clientAccessTokenService;

	public Response getLoginUrl() {

		// hierher ausgelagert, damit ClientAccessTokenService testbar wird.
		String nonce = UUID.randomUUID().toString();
		String accessToken = clientAccessTokenService.orderAccessToken(nonce);

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String redirectUrl = authAppUrl + "/login?accessToken=" + accessToken + "&state=login&redirectUrl="
			+ publicRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(MessagePayload.info(redirectUrl)).build();
	}

	public Response getSignupUrl() {

		// hierher ausgelagert, damit ClientAccessTokenService testbar wird.
		String nonce = UUID.randomUUID().toString();
		String accessToken = clientAccessTokenService.orderAccessToken(nonce);

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String redirectUrl = authAppUrl + "/signup?accessToken=" + accessToken + "&state=signup&redirectUrl="
			+ publicRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(MessagePayload.info(redirectUrl)).build();
	}
}
