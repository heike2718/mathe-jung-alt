// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.NewCookie;

import de.egladil.mja_api.domain.auth.config.AuthConstants;

/**
 * CsrfCookieService
 */
@ApplicationScoped
public class CsrfCookieService {

	@Inject
	SecureTokenService csrfTokenService;

	/**
	 * Erzeugt ein neues CsrfToken.
	 *
	 * @return
	 */
	public NewCookie createCsrfTokenCookie() {

		String csrfToken = csrfTokenService.createRandomToken().replaceAll("\"", "");

		// httpOnly = false
		// secure = true

		return new NewCookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, csrfToken, "/", null, null, -1, true, false);
	}
}
