// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * RaetselgruppenSortattribute
 */
@Schema(
	description = "Attribute, nach denen Rätselgruppen bei der Suche sortiert werden können. Es wird immer nur nach genau einem Attribut sortiert")
public enum RaetselgruppenSortattribute {

	name,
	referenz,
	referenztyp,
	schwierigkeitsgrad
}
