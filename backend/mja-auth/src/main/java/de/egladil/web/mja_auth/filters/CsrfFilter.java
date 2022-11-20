// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.config.AuthConstants;
import de.egladil.web.mja_auth.config.ConfigService;
import de.egladil.web.mja_auth.session.Session;
import de.egladil.web.mja_auth.session.SessionService;
import de.egladil.web.mja_auth.session.SessionUtils;

/**
 * CsrfFilter
 */
// @ApplicationScoped
// @Provider
// @PreMatching
public class CsrfFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfFilter.class);

	@Inject
	ConfigService configService;

	@Inject
	SessionService sessionService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		if (!ConfigService.STAGE_PROD.equals(configService.getStage()) && !configService.isCsrfEnabled()) {

			LOGGER.warn("Achtung: keine csrf protection: check properties 'stage' und 'csrf.enabled' [stage={}, csrfEnabled=",
				configService.getStage(), configService.isCsrfEnabled());
			return;
		}

		String method = requestContext.getMethod();

		if ("OPTIONS".equals(method)) {

			LOGGER.debug("keine CSRF bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();
		LOGGER.debug("entering CsrfFilter: path={}", path);

		String csrfHeader = requestContext.getHeaderString(AuthConstants.CSRF_TOKEN_HEADER_NAME);

		if (csrfHeader == null) {

			return;
		}

		String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());
		Session session = sessionService.getSessionNullSave(sessionId);

		if (session == null) {

			return;
		}

		// String userCsrfToken = session.getUser().getCsrfToken();
		//
		// if (userCsrfToken == null) {
		//
		// return;
		// }
		//
		// if (!csrfHeader.equals(userCsrfToken)) {
		//
		// LOGGER.info("X-XSRF-TOKEN={}, session.crfToken={}", csrfHeader, userCsrfToken);
		// throw new AuthException("csrfHeader does not match the csrf-value in session");
		// }

		return;

	}

}
