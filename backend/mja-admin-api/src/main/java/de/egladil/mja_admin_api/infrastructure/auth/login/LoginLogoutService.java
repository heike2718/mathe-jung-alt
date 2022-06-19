// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.login;

import javax.ws.rs.core.Response;

import de.egladil.mja_admin_api.infrastructure.auth.dto.AuthResult;

/**
 * LoginLogoutService
 */
public interface LoginLogoutService {

	/**
	 * Tauscht das authToken gegen ein JWT und erzeugt eine Session.
	 *
	 * @param  authResult
	 * @param  authMode
	 *                    AuthMode
	 * @return            Response
	 */
	Response login(final AuthResult authResult);

	/**
	 * Logout in Prod, also mit Cookies.
	 *
	 * @param  sessionId
	 * @return           Response
	 */
	Response logout(final String sessionId);

	/**
	 * Logout in Dev, also mit sessionId.
	 *
	 * @param  sessionId
	 * @return           Response
	 */
	Response logoutDev(final String sessionId);

}
