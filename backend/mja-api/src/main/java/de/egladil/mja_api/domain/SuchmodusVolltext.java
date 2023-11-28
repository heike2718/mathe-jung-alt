// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain;

/**
 * SuchmodusVolltext
 */
public enum SuchmodusVolltext {

	INTERSECTION {

		@Override
		public String getOperator() {

			return " AND ";
		}

	},
	UNION {

		@Override
		public String getOperator() {

			return " OR ";
		}

	};

	public abstract String getOperator();

	/**
	 * Machen explizit, dass UNION der default ist. INTERSECTION wird vom Frontend nicht bedient. Es ist auch fraglich, ob das
	 * überhaupt sinnvoll ist.
	 *
	 * @return
	 */
	public static SuchmodusVolltext getDefault() {

		return SuchmodusVolltext.UNION;
	}

}
