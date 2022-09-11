// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

/**
 * SortDirection
 */
public enum SortDirection {

	asc,
	desc,
	noop {

		@Override
		public boolean isDirection() {

			return false;
		}

	};

	public boolean isDirection() {

		return true;
	}

}
