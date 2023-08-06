// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login.impl;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.ClientType;
import de.egladil.mja_api.domain.auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.mja_api.domain.auth.config.ConfigService;
import de.egladil.mja_api.domain.auth.dto.AuthResult;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import de.egladil.mja_api.domain.auth.login.LoginLogoutService;
import de.egladil.mja_api.domain.auth.login.TokenExchangeService;
import de.egladil.mja_api.domain.auth.session.Session;
import de.egladil.mja_api.domain.auth.session.SessionService;
import de.egladil.mja_api.domain.auth.session.SessionUtils;
import de.egladil.mja_api.domain.auth.util.CsrfCookieService;

/**
 * LoginLogoutServiceImpl
 */
@RequestScoped
public class LoginLogoutServiceImpl implements LoginLogoutService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutServiceImpl.class);

	@Inject
	OAuthClientCredentialsProvider clientCredentialsProvider;

	@Inject
	SessionService sessionService;

	@Inject
	TokenExchangeService tokenExchangeService;

	@Inject
	ConfigService configService;

	@Inject
	CsrfCookieService csrfCookieService;

	@Override
	public Response login(final ClientType clientType, final AuthResult authResult, final boolean needsRoleAdmin) {

		if (authResult == null) {

			LOGGER.warn("login wurde ohne payload aufgerufen");

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("login: erwarte authResult")).build());
		}

		String oneTimeToken = authResult.getIdToken();

		LOGGER.debug("idToken={}", StringUtils.abbreviate(oneTimeToken, 11));

		OAuthClientCredentials clientCredentials = clientCredentialsProvider.getClientCredentials(clientType, null);

		String jwt = this.tokenExchangeService.exchangeTheOneTimeToken(clientCredentials.getClientId(),
			clientCredentials.getClientSecret(), oneTimeToken);

		Session session = this.sessionService.initSession(jwt, needsRoleAdmin);

		if (session.isAnonym()) {

			session.clearSessionIdInProd();
			return Response.status(Status.FORBIDDEN)
				.entity(MessagePayload.error("Sie haben leider keine Berechtigung, sich in die Administration einzuloggen."))
				.build();
		}

		NewCookie sessionCookie = SessionUtils.createSessionCookie(session.getSessionId());

		if (!ConfigService.STAGE_DEV.equals(configService.getStage())) {

			session.clearSessionIdInProd();
		}

		return Response.ok(session).cookie(csrfCookieService.createCsrfTokenCookie()).cookie(sessionCookie).build();
	}

	@Override
	public Response logout(final String sessionId) {

		this.sessionService.invalidateSession(sessionId);

		NewCookie invalidatedSessionCookie = SessionUtils.createSessionInvalidatedCookie();

		return Response.ok(MessagePayload.info("erfolgreich ausgeloggt")).cookie(csrfCookieService.createCsrfTokenCookie())
			.cookie(invalidatedSessionCookie).build();
	}

	@Override
	public Response logoutDev(final String sessionId) {

		this.sessionService.invalidateSession(sessionId);

		if (!ConfigService.STAGE_DEV.equals(configService.getStage())) {

			LOGGER.warn("stage={}" + configService.getStage());
			return Response.status(401)
				.entity(MessagePayload.error("böse böse. Dieser Request wurde geloggt!"))
				.cookie(SessionUtils.createSessionInvalidatedCookie()).build();
		}

		return Response.ok(MessagePayload.info("erfolgreich ausgeloggt")).build();

	}
}
