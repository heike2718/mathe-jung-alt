// =====================================================
// Project: mja-shared
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_shared.exceptions;

/**
 * MjaRuntimeException
 */
public class MjaRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public MjaRuntimeException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public MjaRuntimeException(final String message) {

		super(message);

	}

}
