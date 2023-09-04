// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

/**
 * Schriftgroesse
 */
public enum Schriftgroesse {

	HUGE {

		@Override
		public String getLaTeXReplacement() {

			return "\\LARGE";
		}

		@Override
		public String getArrayStretch() {

			return "\\renewcommand\\arraystretch{1.1}";
		}

	},
	NORMAL {

		@Override
		public String getLaTeXReplacement() {

			return "";
		}

		@Override
		public String getArrayStretch() {

			return "\\renewcommand\\arraystretch{1.25}";
		}

	},
	LARGE {

		@Override
		public String getLaTeXReplacement() {

			return "\\Large";
		}

		@Override
		public String getArrayStretch() {

			return "\\renewcommand\\arraystretch{1.1}";
		}

	};

	public abstract String getLaTeXReplacement();

	/**
	 * Die Abstände in Tabellen etc werden etwas vergrößert.
	 *
	 * @return String
	 */
	public abstract String getArrayStretch();

}
