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

		String method = requestContext.getMethod();

		if ("OPTIONS".equals(method)) {

			LOGGER.debug("keine Auth bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("entering InitSecurityContextFilter: path={}", path);

		String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());

		if (sessionId != null) {

			Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

			if (session != null) {

				boolean secure = !configService.getStage().equals(ConfigService.STAGE_DEV);
				requestContext.setSecurityContext(new MjaSecurityContext(session, secure));
				LOGGER.info("securityContext set");

			}
		}
	}
}
