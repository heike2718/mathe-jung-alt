// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.cdi;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import jakarta.enterprise.context.RequestScoped;

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

			LOGGER.warn("dem User {} fehlt die Rolle {}", user.getName(), role);
		}

		return optRole.isPresent();
	}

}
