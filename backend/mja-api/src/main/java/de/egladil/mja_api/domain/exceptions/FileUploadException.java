// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.exceptions;

/**
 * FileUploadException
 */
public class FileUploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public FileUploadException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public FileUploadException(final String message) {

		super(message);

	}

}
