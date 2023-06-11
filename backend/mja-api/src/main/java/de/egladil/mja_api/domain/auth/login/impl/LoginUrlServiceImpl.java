// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login.impl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.ClientType;
import de.egladil.mja_api.domain.auth.clientauth.ClientAccessTokenService;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.login.LoginUrlService;

/**
 * LoginUrlServiceImpl
 */
@RequestScoped
public class LoginUrlServiceImpl implements LoginUrlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUrlServiceImpl.class);

	@ConfigProperty(name = "auth-app.url")
	String authAppUrl;

	@ConfigProperty(name = "admin-redirect-url.login")
	String adminLoginRedirectUrl;

	@ConfigProperty(name = "public-redirect-url.login")
	String publicLoginRedirectUrl;

	@Inject
	ClientAccessTokenService clientAccessTokenService;

	@Override
	public Response getLoginUrl(final ClientType clientType) {

		String accessToken = clientAccessTokenService.orderAccessToken(clientType);

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String loginRedirectUrl = getLoginRedirectUrl(clientType);

		LOGGER.info("loginRedirectUrl={}", loginRedirectUrl);

		String redirectUrl = authAppUrl + "#/login?accessToken=" + accessToken + "&state=login&redirectUrl="
			+ loginRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(MessagePayload.info(redirectUrl)).build();
	}

	/**
	 * @param  clientType
	 * @return
	 */
	private String getLoginRedirectUrl(final ClientType clientType) {

		switch (clientType) {

			case ADMIN:
				return adminLoginRedirectUrl;

			case PUBLIC:
				return publicLoginRedirectUrl;

			default:
				break;

		}

		throw new IllegalArgumentException("unerwarteter ClientType " + clientType);
	}
}
