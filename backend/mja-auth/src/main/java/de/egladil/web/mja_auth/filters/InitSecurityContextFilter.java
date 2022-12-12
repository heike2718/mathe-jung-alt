// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.config.ConfigService;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.MjaSecurityContext;
import de.egladil.web.mja_auth.session.Session;
import de.egladil.web.mja_auth.session.SessionService;
import de.egladil.web.mja_auth.session.SessionUtils;

/**
 * InitSecurityContextFilter
 */
@ApplicationScoped
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class InitSecurityContextFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitSecurityContextFilter.class);

	@Inject
	ConfigService configService;

	@Inject
	SessionService sessionService;

	@Context
	ResourceInfo resourceInfo;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		LOGGER.debug("stage=" + configService.getStage() + ", mockSession=" + configService.isMockSession());

		String method = requestContext.getMethod();

		if ("OPTIONS".equals(method)) {

			LOGGER.debug("keine Auth bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("entering InitSecurityContextFilter: path={}", path);

		if (!ConfigService.STAGE_PROD.equals(configService.getStage()) && configService.isMockSession()) {

			LOGGER.warn("Achtung: mock-Session!!! check properties 'stage' und 'mock.session' [stage={}, mockSession=",
				configService.getStage(), configService.isMockSession());

			initMockSecurityContext(requestContext);
		} else {

			// String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());
			String sessionId = SessionUtils.getSessionIdFromCookie(requestContext);

			if (sessionId != null) {

				Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

				if (session != null) {

					boolean secure = !configService.getStage().equals(ConfigService.STAGE_DEV);
					requestContext.setSecurityContext(new MjaSecurityContext(session, secure));
					LOGGER.debug("securityContext set");

				}
			}
		}
	}

	private void initMockSecurityContext(final ContainerRequestContext requestContext) {

		AuthenticatedUser user = new AuthenticatedUser("bla", new String[] { "ADMIN" }, "Heike Winkelvoß",
			"b865fc75-1bcf-40c7-96c3-33744826e49f");
		Session session = new Session().withUser(user);
		boolean secure = !configService.getStage().equals(ConfigService.STAGE_DEV);
		requestContext.setSecurityContext(new MjaSecurityContext(session, secure));
		LOGGER.warn("config property 'mock.session' is true => SecurityContext with mocker user: ");
	}
}
