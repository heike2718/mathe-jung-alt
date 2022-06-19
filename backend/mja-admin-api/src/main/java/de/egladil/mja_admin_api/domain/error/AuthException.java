// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.error;

/**
 * AuthException
 */
public class AuthException extends RuntimeException {

	/**
	 * @param message
	 * @param cause
	 */
	public AuthException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public AuthException(final String message) {

		super(message);

	}

	private static final long serialVersionUID = -1906597753895801273L;

}
