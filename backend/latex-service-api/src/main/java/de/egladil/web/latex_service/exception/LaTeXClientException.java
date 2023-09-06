// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service.exception;

/**
 * LaTeXClientException
 */
public class LaTeXClientException extends RuntimeException {

	private static final long serialVersionUID = 3075391950576409418L;

	/**
	 * @param message
	 * @param cause
	 */
	public LaTeXClientException(final String message, final Throwable cause) {

		super(message, cause);

	}

	/**
	 * @param message
	 */
	public LaTeXClientException(final String message) {

		super(message);

	}

}
