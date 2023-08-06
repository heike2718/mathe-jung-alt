// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MjaSecurityContext
 */
@Deprecated
public class MjaSecurityContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(MjaSecurityContext.class);

	private Session session;

	private boolean secure;

	/**
	 *
	 */
	public MjaSecurityContext() {

		super();

	}

	public Principal getUserPrincipal() {

		if (session == null || session.isAnonym()) {

			return null;
		}

		return session.getUser();
	}

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

	public boolean isSecure() {

		return this.secure;
	}

	public String getAuthenticationScheme() {

		return "Bearer";
	}

	public MjaSecurityContext withSession(final Session session) {

		this.session = session;
		return this;
	}

	public MjaSecurityContext withSecure(final boolean secure) {

		this.secure = secure;
		return this;
	}
}
