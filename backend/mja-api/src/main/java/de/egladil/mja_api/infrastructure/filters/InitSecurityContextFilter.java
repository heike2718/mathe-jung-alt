// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.MjaApiApplication;
import de.egladil.mja_api.domain.auth.config.ConfigService;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Session;
import de.egladil.mja_api.domain.auth.session.SessionService;
import de.egladil.mja_api.domain.auth.session.SessionUtils;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

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

			String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());

			if (sessionId != null) {

				Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

				if (session != null) {

					// Packen den User in die Session.
					requestContext.setProperty(MjaApiApplication.USER, session.getUser());
					LOGGER.debug("user set");

				}
			}
		}
	}

	private void initMockSecurityContext(final ContainerRequestContext requestContext) {

		AuthenticatedUser user = new AuthenticatedUser("b865fc75-1bcf-40c7-96c3-33744826e49f").withFullName("Heike Winkelvoß")
			.withIdReference("bla").withRoles(new String[] { "ADMIN" });

		requestContext.setProperty(MjaApiApplication.USER, user);
		LOGGER.warn("config property 'mock.session' is true => SecurityContext with mocker user: ");
	}
}
