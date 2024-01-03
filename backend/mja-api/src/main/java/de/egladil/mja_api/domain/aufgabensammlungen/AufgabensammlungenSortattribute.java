// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * AufgabensammlungenSortattribute
 */
@Schema(
	description = "Attribute, nach denen Aufgabensammlungen bei der Suche sortiert werden können. Es wird immer nur nach genau einem Attribut sortiert")
public enum AufgabensammlungenSortattribute {

	name,
	referenz,
	referenztyp,
	schwierigkeitsgrad
}
