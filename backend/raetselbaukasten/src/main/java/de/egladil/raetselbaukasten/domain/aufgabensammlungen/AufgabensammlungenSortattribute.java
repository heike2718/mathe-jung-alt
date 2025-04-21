// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.aufgabensammlungen;

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
