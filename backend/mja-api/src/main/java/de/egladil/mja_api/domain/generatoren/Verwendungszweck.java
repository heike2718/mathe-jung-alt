// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

/**
 * Verwendungszweck. Er bestimmt die Gruppierung der QuizItems im PDF oder LaTeX.
 */
public enum Verwendungszweck {

	ARBEITSBLATT,
	KARTEI,
	LATEX,
	VORSCHAU {

		@Override
		public boolean isHeadersWithNummerUndSchluessel() {

			return true;
		}
	};

	/**
	 * @return boolean default ist false
	 */
	public boolean isHeadersWithNummerUndSchluessel() {

		return false;
	}
}
