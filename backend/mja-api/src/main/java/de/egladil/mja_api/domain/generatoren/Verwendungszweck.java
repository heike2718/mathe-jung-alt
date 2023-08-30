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
	AUFGABEN_MIT_LOESUNGEN,
	KARTEI,
	LATEX {

		@Override
		public boolean compileToPDF() {

			return false;
		}

	},
	VORSCHAU {

		@Override
		public boolean isHeadersWithNummerUndSchluessel() {

			return true;
		}
	};

	/**
	 * Gibt an, ob gleich compiliert werden soll.
	 *
	 * @return
	 */
	public boolean compileToPDF() {

		return true;
	};

	/**
	 * @return boolean default ist false
	 */
	public boolean isHeadersWithNummerUndSchluessel() {

		return false;
	}
}
