// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.File;

import de.egladil.web.latex_cli.exceptions.LaTeXCLIRuntimeException;

/**
 * FileProcessor
 */
public interface FileProcessor {

	/**
	 * Nimmt ein File entgegen und erzeugt daraus ein transformiertes File.
	 *
	 * @param  file
	 *              File
	 * @return      File
	 */
	File transform(File file) throws LaTeXCLIRuntimeException;

}
