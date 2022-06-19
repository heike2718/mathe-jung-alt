// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.error;

/**
 * SessionExpiredException
 */
public class SessionExpiredException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public SessionExpiredException(final String message) {

		super(message);
	}

}
