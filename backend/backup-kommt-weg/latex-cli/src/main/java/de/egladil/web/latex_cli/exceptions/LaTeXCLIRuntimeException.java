// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.exceptions;

/**
 * LaTeXCLIRuntimeException
 */
public class LaTeXCLIRuntimeException extends RuntimeException {

	private final int exitCode;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public LaTeXCLIRuntimeException(final int exitCode, final String message, final Throwable cause) {

		super(message, cause);
		this.exitCode = exitCode;

	}

	/**
	 * @param message
	 */
	public LaTeXCLIRuntimeException(final int exitCode, final String message) {

		super(message);
		this.exitCode = exitCode;

	}

	public int getExitCode() {

		return exitCode;
	}

}
