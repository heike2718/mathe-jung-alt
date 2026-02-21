// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.aufgabensammlungen;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.domain.dto.SortDirection;

/**
 * AufgabensammlungenSuchparameter
 */

public record AufgabensammlungenSuchparameter(
// @formatter:off
	String name,
	Schwierigkeitsgrad schwierigkeitsgrad,
	Referenztyp referenztyp,
	String referenz,
	AufgabensammlungenSortattribute sortAttribute,
	SortDirection sortDirection,
	String owner,
	Benutzerart benutzerart
	) {
	// @formatter:on

    public boolean isLeer() {

        return StringUtils.isBlank(name) && StringUtils.isBlank(referenz) && referenztyp == null
                && schwierigkeitsgrad == null;
    }
}
