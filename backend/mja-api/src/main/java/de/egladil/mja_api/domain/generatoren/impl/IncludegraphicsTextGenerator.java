// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

/**
 * IncludegraphicsTextGenerator
 */
public class IncludegraphicsTextGenerator {

	/**
	 * Generiert den \includegraphics - Befehl für LaTeX.
	 *
	 * @param  relativePath
	 *                      String der relative Pfad zum EPS-File ohne Punkt
	 * @return              String
	 */
	public String generateIncludegraphicsText(final String relativePath) {

		return LaTeXConstants.INCLUDEGRAPHICS_START + relativePath + LaTeXConstants.INCLUDEGRAPHICS_END;
	}

}
