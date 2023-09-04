// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

/**
 * FontName
 */
public enum FontName {

	DRUCK_BY_WOK {

		@Override
		public String getLatexFileInputDefinition() {

			return "\\input{./include/druck-by-wok}";
		}

	},

	FIBEL_NORD {

		@Override
		public String getLatexFileInputDefinition() {

			return "\\input{./include/fibel-nord}";
		}

	},
	FIBEL_SUED {

		@Override
		public String getLatexFileInputDefinition() {

			return "\\input{./include/fibel-sued}";
		}

	},
	STANDARD {

		@Override
		public String getLatexFileInputDefinition() {

			return "";
		}

	};

	/**
	 * Das, wodurch {font} im LaTeX-Template ersetzt werden soll.
	 *
	 * @return String
	 */
	public abstract String getLatexFileInputDefinition();

}
