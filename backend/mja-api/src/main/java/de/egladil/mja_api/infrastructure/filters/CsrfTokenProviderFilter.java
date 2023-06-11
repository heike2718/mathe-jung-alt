// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.auth.util.CsrfCookieService;

/**
 * CsrfTokenProviderFilter
 */
@Provider
public class CsrfTokenProviderFilter implements ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenProviderFilter.class);

	@Inject
	CsrfCookieService csrfCookieService;

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

		LOGGER.debug("==> entering");

		if (requestContext.getCookies().containsKey(AuthConstants.CSRF_TOKEN_COOKIE_NAME)) {

			return;
		}
		responseContext.getHeaders().add("Set-Cookie", csrfCookieService.createCsrfTokenCookie());
	}
}
