// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login;

import de.egladil.mja_api.domain.auth.dto.AuthResult;
import jakarta.ws.rs.core.Response;

/**
 * LoginLogoutService
 */
public interface LoginLogoutService {

	/**
	 * Tauscht das authToken gegen ein JWT und erzeugt eine Session.
	 *
	 * @param  authResult
	 * @param  needsRoleAdmin
	 *                        boolean
	 * @param  authMode
	 *                        AuthMode
	 * @return                Response
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
