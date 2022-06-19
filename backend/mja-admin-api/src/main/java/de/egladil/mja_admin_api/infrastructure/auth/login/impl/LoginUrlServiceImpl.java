// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.login.impl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.dto.MessagePayload;
import de.egladil.mja_admin_api.domain.dto.ResponsePayload;
import de.egladil.mja_admin_api.infrastructure.auth.clientauth.ClientAccessTokenService;
import de.egladil.mja_admin_api.infrastructure.auth.login.LoginUrlService;

/**
 * LoginUrlServiceImpl
 */
@RequestScoped
public class LoginUrlServiceImpl implements LoginUrlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUrlServiceImpl.class);

	@ConfigProperty(name = "auth-app.url")
	String authAppUrl;

	@ConfigProperty(name = "redirect-url.login")
	String loginRedirectUrl;

	@Inject
	ClientAccessTokenService clientAccessTokenService;

	@Override
	public Response getLoginUrl() {

		String accessToken = clientAccessTokenService.orderAccessToken();

		if (StringUtils.isBlank(accessToken)) {

			return Response.serverError().entity("Fehler beim Authentisieren des Clients").build();
		}

		String redirectUrl = authAppUrl + "#/login?accessToken=" + accessToken + "&state=login&redirectUrl="
			+ loginRedirectUrl;

		LOGGER.debug(redirectUrl);

		return Response.ok(ResponsePayload.messageOnly(MessagePayload.info(redirectUrl))).build();
	}
}
