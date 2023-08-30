// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import de.egladil.mja_api.domain.generatoren.impl.LaTeXConstants;

/**
 * TrennerartFrageLoesung
 */
public enum TrennerartFrageLoesung {

	ABSTAND {

		@Override
		public String getLeTeX() {

			return LaTeXConstants.VALUE_PAR;
		}

	},
	SEITENUMBRUCH {

		@Override
		public String getLeTeX() {

			return LaTeXConstants.VALUE_NEWPAGE;
		}
	};

	/**
	 * @return String das LaTeX-Schnipsel für diese Trennerart.
	 */
	public abstract String getLeTeX();
}
