// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.exceptions;

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
