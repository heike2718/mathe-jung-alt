// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.cdi;

import java.util.Arrays;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;

/**
 * AuthenticationContextImpl
 */
@RequestScoped
public class AuthenticationContextImpl implements AuthenticationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationContextImpl.class);

    private AuthenticatedUser user;

    @Override
    public AuthenticatedUser getUser() {

        return this.user;
    }

    public void setUser(final AuthenticatedUser user) {

        this.user = user;
    }

    @Override
    public boolean isUserInRole(final String role) {

        Optional<String> optRole = Arrays.stream(user.getRoles()).filter(r -> r.equalsIgnoreCase(role)).findFirst();

        if (optRole.isEmpty()) {

            LOGGER.debug("dem User {} fehlt die Rolle {}", user.getName(), role);
        }

        return optRole.isPresent();
    }

}
