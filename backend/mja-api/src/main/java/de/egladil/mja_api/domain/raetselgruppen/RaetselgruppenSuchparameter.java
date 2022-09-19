// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.dto.SortDirection;

/**
 * RaetselgruppenSuchparameter
 */

public record RaetselgruppenSuchparameter(
// @formatter:off
	String name,
	Schwierigkeitsgrad schwierigkeitsgrad,
	Referenztyp referenztyp,
	String referenz,
	RaetselgruppenSortattribute sortAttribute,
	SortDirection sortDirection
	) {
	// @formatter:on

	public boolean isLeer() {

		return StringUtils.isBlank(name) && StringUtils.isBlank(referenz) && referenztyp == null && schwierigkeitsgrad == null;
	}
}
