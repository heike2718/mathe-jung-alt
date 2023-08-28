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
	 * Default ist ""
	 *
	 * @return String
	 */
	public abstract String getLatexFileInputDefinition();
}
