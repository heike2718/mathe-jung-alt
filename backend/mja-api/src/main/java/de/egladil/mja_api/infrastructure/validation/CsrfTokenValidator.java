// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.exception.AuthException;

/**
 * CsrfTokenValidator
 */
public class CsrfTokenValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfTokenValidator.class);

	/**
	 * Prüft das CSRF-Token.
	 *
	 * @param headerValue
	 *                    String das token aus dem Header
	 * @param token
	 *                    String das aus der Session
	 * @param enabled
	 *                    boolean
	 */
	public void checkCsrfToken(final String headerValue, final String token, final boolean enabled) throws AuthException {

		if (!enabled) {

			LOGGER.warn("authorization is diadabled: check property authorization.enabled");

			return;
		}

		if (headerValue == null || !headerValue.equals(token)) {

			LOGGER.info("X-XSRF-TOKEN={}, session.crfToken={}", headerValue, token);
			throw new AuthException("csrfHeader does not match the csrf-value in session");
		}
	}

}
