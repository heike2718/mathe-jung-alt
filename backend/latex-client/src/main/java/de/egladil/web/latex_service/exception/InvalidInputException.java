// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service.exception;

import java.util.List;

/**
 * InvalidInputException
 */
public class InvalidInputException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public final List<String> invalidInputs;

	/**
	 * @param message
	 */
	public InvalidInputException(final List<String> invalidInputs, final String message) {

		super(message);
		this.invalidInputs = invalidInputs;

	}

	public String getInvalidInputs() {

		final StringBuilder sb = new StringBuilder();
		invalidInputs.forEach(i -> sb.append(i));
		return sb.toString();
	}

}
