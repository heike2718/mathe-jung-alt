// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.session;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MjaSecurityContext
 */
public class MjaSecurityContext implements SecurityContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(MjaSecurityContext.class);

	private final Session session;

	private final boolean secure;

	public MjaSecurityContext(final Session session, final boolean secure) {

		this.session = session;
		this.secure = secure;
	}

	@Override
	public Principal getUserPrincipal() {

		if (session == null || session.isAnonym()) {

			return null;
		}

		return session.getUser();
	}

	@Override
	public boolean isUserInRole(final String role) {

		if (session == null || session.isAnonym()) {

			LOGGER.warn("session ist anonym");

			return false;
		}

		AuthenticatedUser user = session.getUser();

		Optional<String> optRole = Arrays.stream(user.getRoles()).filter(r -> r.equalsIgnoreCase(role)).findFirst();

		if (optRole.isEmpty()) {

			LOGGER.warn("dem User {} fehlt die Rolle {}", user.getUuid(), role);
		}

		return optRole.isPresent();
	}

	@Override
	public boolean isSecure() {

		return this.secure;
	}

	@Override
	public String getAuthenticationScheme() {

		return "Bearer";
	}

}
