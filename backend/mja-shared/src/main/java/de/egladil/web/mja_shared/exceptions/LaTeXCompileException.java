// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_shared.exceptions;

/**
 * LaTeXCompileException
 */
public class LaTeXCompileException extends RuntimeException {

	private static final long serialVersionUID = -5534311990730576744L;

	private String nameFile;

	/**
	 * @param message
	 */
	public LaTeXCompileException(final String message) {

		super(message);

	}

	public String getNameFile() {

		return nameFile;
	}

	public LaTeXCompileException withNameFile(final String nameFile) {

		this.nameFile = nameFile;
		return this;
	}

}
