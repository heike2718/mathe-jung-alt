// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.config.AuthConstants;
import de.egladil.web.mja_auth.config.ConfigService;

/**
 * CsrfTokenValidationFilter
 */
@ApplicationScoped
@Provider
@PreMatching
public class CsrfTokenValidationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenValidationFilter.class);

	private static final Response INVALID_CSRF_TOKEN_RESPONSE = Response.status(Response.Status.BAD_REQUEST)
		.entity("CSRF-Token-Validierung fehlgeschlagen. Brauchen CSRF-Token im Header: " + AuthConstants.CSRF_TOKEN_HEADER_NAME
			+ " und cookie: " + AuthConstants.CSRF_TOKEN_COOKIE_NAME)
		.build();

	private static final List<String> SECURE_HTTP_METHODS = Arrays.asList(new String[] { "OPTIONS", "GET", "HEAD" });

	@Inject
	ConfigService configService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		LOGGER.debug("entering validation filter");

		if (!configService.isCsrfEnabled()) {

			LOGGER.warn("Achtung: keine csrf protection: check property 'csrf.enabled' [csrfEnabled={}]",
				configService.isCsrfEnabled());
			return;
		}

		if (SECURE_HTTP_METHODS.contains(requestContext.getMethod())) {

			return;
		}

		Cookie csrfTokenCookie = requestContext.getCookies().get(AuthConstants.CSRF_TOKEN_COOKIE_NAME);
		List<String> csrfTokenHeader = requestContext.getHeaders().get(AuthConstants.CSRF_TOKEN_HEADER_NAME);

		if (csrfTokenCookie == null || csrfTokenHeader == null || csrfTokenHeader.size() != 1
			|| !csrfTokenHeader.get(0).equals(csrfTokenCookie.getValue())) {

			requestContext.abortWith(INVALID_CSRF_TOKEN_RESPONSE);
		}

		return;
	}

}
