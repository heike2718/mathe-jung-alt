// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;

import de.egladil.web.mja_auth.config.AuthConstants;

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
