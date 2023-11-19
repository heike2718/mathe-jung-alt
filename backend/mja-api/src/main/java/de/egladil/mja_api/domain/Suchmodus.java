// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain;

/**
 * Suchmodus
 */
public enum Suchmodus {

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

}
