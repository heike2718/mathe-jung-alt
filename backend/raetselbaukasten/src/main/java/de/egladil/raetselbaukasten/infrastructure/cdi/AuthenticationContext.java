// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.cdi;

import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;

/**
 * AuthenticationContext
 */
public interface AuthenticationContext {

    /**
     * Der admin wird vom InitSecurityContextFilter in den AuthenticationContext
     * gepackt und hier dann herausgeholt.
     *
     * @return
     */
    AuthenticatedUser getUser();

    /**
     * @param role
     * @return boolean
     */
    boolean isUserInRole(String role);
}
