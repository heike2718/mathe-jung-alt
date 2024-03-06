// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.config.ConfigService;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * CsrfTokenValidationFilter
 */
@ApplicationScoped
@Provider
@PreMatching
public class CsrfTokenValidationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenValidationFilter.class);

	private static final String INVALID_CSRF_RESPONSE_PAYLOD = "CSRF-Token-Validierung fehlgeschlagen";

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

			LOGGER.warn("csrfTokenCookie == null bei path={}", path);
			requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
		}

		List<String> csrfTokenHeader = requestContext.getHeaders().get(AuthConstants.CSRF_TOKEN_HEADER_NAME);

		if (csrfTokenHeader == null || csrfTokenHeader.isEmpty()) {

			LOGGER.warn("csrfTokenHeader == null oder csrfTokenHeader.size() != 1 bei path={}", path);
			requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
		}

		String headerValue = csrfTokenHeader.get(0);
		String cookieValue = csrfTokenCookie.getValue();

		if (!identifyAsEquals(headerValue, cookieValue)) {

			LOGGER.warn("cookieValue != headerValue: [headerValue={}, cookieValue={}, path={}", headerValue, cookieValue, path);
			requestContext.abortWith(Response.status(400).entity(MessagePayload.error(INVALID_CSRF_RESPONSE_PAYLOD)).build());
		}
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
