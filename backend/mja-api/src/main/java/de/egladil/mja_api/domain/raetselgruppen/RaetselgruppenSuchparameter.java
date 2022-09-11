// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.apache.commons.lang3.StringUtils;

/**
 * RaetselgruppenSuchparameter
 */

public record RaetselgruppenSuchparameter(
// @formatter:off
	String name,
	Schwierigkeitsgrad schwierigkeitsgrad,
	Referenztyp referenztyp,
	String referenz
//	,
//	SortDirection sortName,
//	SortDirection sortSchwierigkeitsgrad,
//	SortDirection sortReferenztyp,
//	SortDirection sortReferenz
	) {
	// @formatter:on

	public boolean isLeer() {

		return StringUtils.isBlank(name) && StringUtils.isBlank(referenz) && referenztyp == null && schwierigkeitsgrad == null;
	}

	public boolean hatSortierung() {

		// return sortName.isDirection() || sortReferenz.isDirection() || sortReferenztyp.isDirection()
		// || sortSchwierigkeitsgrad.isDirection();

		return false;
	}

}
