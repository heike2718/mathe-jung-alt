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

	private static final Response INVALID_CSRF_HEADER_RESPONSE = Response.status(Response.Status.BAD_REQUEST)
		.entity("CSRF-Token-Validierung fehlgeschlagen. kein oder mehr als ein CSRF-Token-Header-Value: "
			+ AuthConstants.CSRF_TOKEN_HEADER_NAME)
		.build();

	private static final Response INVALID_CSRF_TOKEN_RESPONSE = Response.status(Response.Status.BAD_REQUEST)
		.entity("CSRF-Token-Validierung fehlgeschlagen. Brauchen CSRF-Token im Header: " + AuthConstants.CSRF_TOKEN_HEADER_NAME
			+ " und cookie: " + AuthConstants.CSRF_TOKEN_COOKIE_NAME)
		.build();

	private static final List<String> SECURE_HTTP_METHODS = Arrays.asList(new String[] { "OPTIONS", "GET", "HEAD" });

	private static final List<String> SECURE_PATHS = Arrays.asList(new String[] { "/session/logout" });

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

		String path = requestContext.getUriInfo().getPath();

		if (SECURE_HTTP_METHODS.contains(requestContext.getMethod()) || SECURE_PATHS.contains(path)) {

			return;
		}

		Cookie csrfTokenCookie = requestContext.getCookies().get(AuthConstants.CSRF_TOKEN_COOKIE_NAME);

		if (csrfTokenCookie == null) {

			requestContext.abortWith(INVALID_CSRF_HEADER_RESPONSE);
		}

		List<String> csrfTokenHeader = requestContext.getHeaders().get(AuthConstants.CSRF_TOKEN_HEADER_NAME);

		if (csrfTokenHeader == null || csrfTokenHeader.size() != 1) {

			requestContext.abortWith(INVALID_CSRF_HEADER_RESPONSE);
		}

		String headerValue = csrfTokenHeader.get(0);
		String cookieValue = csrfTokenCookie.getValue();

		if (!identifyAsEquals(headerValue, cookieValue)) {

			LOGGER.warn("[headerValue={}, cookieValue={}", headerValue, cookieValue);
			requestContext.abortWith(INVALID_CSRF_TOKEN_RESPONSE);
		}

		return;
	}

	boolean identifyAsEquals(final String headerValue, final String cookieValue) {

		String strippedHeaderValue = headerValue;

		// ist irgendwie komisch, aber der headerValue kommt mit Anführungszeichen.
		if (headerValue.contains("\"")) {

			strippedHeaderValue = headerValue.replaceAll("\"", "");
		}

		return strippedHeaderValue.equals(cookieValue);
	}

}
