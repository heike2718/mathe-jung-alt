// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.exceptions;

/**
 * MjaAuthRuntimeException
 */
public class MjaAuthRuntimeException extends RuntimeException {

	/**
	 * @param message
	 * @param cause
	 */
	public MjaAuthRuntimeException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public MjaAuthRuntimeException(final String message) {

		super(message);

	}

	private static final long serialVersionUID = -879307140316163244L;

}
