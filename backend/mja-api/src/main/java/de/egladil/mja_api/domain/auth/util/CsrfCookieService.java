// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.NewCookie;;

/**
 * CsrfCookieService
 */
@ApplicationScoped
public class CsrfCookieService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfCookieService.class);

	@Inject
	SecureTokenService csrfTokenService;

	/**
	 * Erzeugt ein neues CsrfToken.
	 *
	 * @return
	 */
	public NewCookie createCsrfTokenCookie() {

		String csrfToken = csrfTokenService.createRandomToken().replaceAll("\"", "");

		LOGGER.debug("csrfToken={}", csrfToken);

		return new NewCookie.Builder(AuthConstants.CSRF_TOKEN_COOKIE_NAME).path("/").comment("csrf").maxAge(-1)
			.httpOnly(false).secure(true).build();
	}
}
