// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.exceptions;

/**
 * MjaAdminServerException
 */
public class MjaAdminServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public MjaAdminServerException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public MjaAdminServerException(final String message) {

		super(message);

	}

}
