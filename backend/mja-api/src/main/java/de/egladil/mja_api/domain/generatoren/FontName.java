// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

/**
 * FontName
 */
public enum FontName {

	FIBEL_NORD {

		@Override
		public String getLatexFileInputDefinition() {

			return "\\input{./include/fibel-nord}";
		}

		@Override
		public String getSchriftgroesse() {

			return "12pt";
		}

	},
	FIBEL_SUED {

		@Override
		public String getLatexFileInputDefinition() {

			return "\\input{./include/fibel-sued}";
		}

		@Override
		public String getSchriftgroesse() {

			return "12pt";
		}

	},
	STANDARD {

		@Override
		public String getLatexFileInputDefinition() {

			return "";
		}

		@Override
		public String getSchriftgroesse() {

			return "11pt";
		}

	};

	/**
	 * Das, wodurch {font} im LaTeX-Template ersetzt werden soll.
	 *
	 * @return String
	 */
	public abstract String getLatexFileInputDefinition();

	/**
	 * Das, wodurch {size} im LaTeX-Template ersetzt werden soll.
	 *
	 * @return String
	 */
	public abstract String getSchriftgroesse();
}
