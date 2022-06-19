// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.exception;

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
