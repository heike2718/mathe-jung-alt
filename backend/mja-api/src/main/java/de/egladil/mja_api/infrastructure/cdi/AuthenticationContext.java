// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.cdi;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;

/**
 * AuthenticationContext
 */
public interface AuthenticationContext {

	/**
	 * Der admin wird vom InitSecurityContextFilter in den AuthenticationContext gepackt und hier dann herausgeholt.
	 *
	 * @return
	 */
	AuthenticatedUser getUser();

	/**
	 * @param  role
	 * @return      boolean
	 */
	boolean isUserInRole(String role);
}
