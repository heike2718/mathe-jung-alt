// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.login.impl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.config.ConfigService;
import de.egladil.web.mja_auth.dto.AuthResult;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.login.LoginLogoutService;
import de.egladil.web.mja_auth.login.TokenExchangeService;
import de.egladil.web.mja_auth.session.Session;
import de.egladil.web.mja_auth.session.SessionService;
import de.egladil.web.mja_auth.session.SessionUtils;

/**
 * LoginLogoutServiceImpl
 */
@RequestScoped
public class LoginLogoutServiceImpl implements LoginLogoutService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutServiceImpl.class);

	@ConfigProperty(name = "client-id")
	String clientId;

	@ConfigProperty(name = "client-secret")
	String clientSecret;

	@Inject
	SessionService sessionService;

	@Inject
	TokenExchangeService tokenExchangeService;

	@Inject
	ConfigService configService;

	@Override
	public Response login(final AuthResult authResult, final boolean needsRoleAdmin) {

		if (authResult == null) {

			LOGGER.warn("login wurde ohne payload aufgerufen");

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("login: erwarte authResult")).build());
		}

		String oneTimeToken = authResult.getIdToken();

		LOGGER.debug("idToken={}", StringUtils.abbreviate(oneTimeToken, 11));

		String jwt = this.tokenExchangeService.exchangeTheOneTimeToken(clientId, clientSecret, oneTimeToken);

		Session session = this.sessionService.initSession(jwt, needsRoleAdmin);

		if (session.isAnonym()) {

			session.clearSessionId();
			return Response.status(Status.FORBIDDEN)
				.entity(MessagePayload.error("Sie haben leider keine Berechtigung, sich in die Administration einzuloggen."))
				.build();
		}

		NewCookie sessionCookie = SessionUtils.createSessionCookie(session.getSessionId());

		if (!ConfigService.STAGE_DEV.equals(configService.getStage())) {

			session.clearSessionId();
		}

		return Response.ok(session).cookie(sessionCookie).build();
	}

	@Override
	public Response logout(final String sessionId) {

		this.sessionService.invalidateSession(sessionId);

		NewCookie invalidatedCookie = SessionUtils.createSessionInvalidatedCookie();

		return Response.ok(MessagePayload.info("erfolgreich ausgeloggt")).cookie(invalidatedCookie).build();
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
