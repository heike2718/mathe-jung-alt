// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.login.impl;

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

import de.egladil.mja_admin_api.MjaAdminApiApplication;
import de.egladil.mja_admin_api.domain.dto.MessagePayload;
import de.egladil.mja_admin_api.infrastructure.auth.dto.AuthResult;
import de.egladil.mja_admin_api.infrastructure.auth.login.LoginLogoutService;
import de.egladil.mja_admin_api.infrastructure.auth.login.TokenExchangeService;
import de.egladil.mja_admin_api.infrastructure.auth.session.Session;
import de.egladil.mja_admin_api.infrastructure.auth.session.SessionService;
import de.egladil.mja_admin_api.infrastructure.auth.session.SessionUtils;
import de.egladil.mja_admin_api.infrastructure.config.ConfigService;

/**
 * LoginLogoutServiceImpl
 */
@RequestScoped
public class LoginLogoutServiceImpl implements LoginLogoutService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutServiceImpl.class);

	@ConfigProperty(name = "stage")
	String stage;

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
	public Response login(final AuthResult authResult) {

		if (authResult == null) {

			LOGGER.warn("login wurde ohne payload aufgerufen");

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("login: erwarte authResult")).build());
		}

		String oneTimeToken = authResult.getIdToken();

		LOGGER.debug("idToken={}", StringUtils.abbreviate(oneTimeToken, 11));

		String jwt = this.tokenExchangeService.exchangeTheOneTimeToken(clientId, clientSecret, oneTimeToken);

		Session session = this.sessionService.initSession(jwt);

		NewCookie sessionCookie = SessionUtils.createSessionCookie(session.getSessionId());

		if (this.stage.equals(configService.getStage())) {

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

		if (!this.stage.equals(MjaAdminApiApplication.STAGE_DEV)) {

			LOGGER.warn("stage={}" + this.stage);
			return Response.status(401)
				.entity(MessagePayload.error("böse böse. Dieser Request wurde geloggt!"))
				.cookie(SessionUtils.createSessionInvalidatedCookie()).build();
		}

		return Response.ok(MessagePayload.info("erfolgreich ausgeloggt")).build();

	}
}
