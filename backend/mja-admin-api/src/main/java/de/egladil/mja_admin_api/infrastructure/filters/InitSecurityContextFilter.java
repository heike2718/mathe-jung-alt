// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.filters;

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

import de.egladil.mja_admin_api.MjaAdminApiApplication;
import de.egladil.mja_admin_api.infrastructure.auth.session.MjaSecurityContext;
import de.egladil.mja_admin_api.infrastructure.auth.session.Session;
import de.egladil.mja_admin_api.infrastructure.auth.session.SessionService;
import de.egladil.mja_admin_api.infrastructure.auth.session.SessionUtils;
import de.egladil.mja_admin_api.infrastructure.config.ConfigService;

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

			LOGGER.info("keine Auth bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.info("entering InitSecurityContextFilter: path={}", path);

		String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());

		if (sessionId != null) {

			Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

			if (session != null) {

				boolean secure = !configService.getStage().equals(MjaAdminApiApplication.STAGE_DEV);
				requestContext.setSecurityContext(new MjaSecurityContext(session, secure));

			}
		}
	}
}
