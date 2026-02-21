// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.aufgabensammlungen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Referenztyp
 */
@Schema(
        name = "Referenztyp",
        description = "Kontext zur Interpretation des Parameters 'referenz', also einer ID im alten Aufgabenarchiv")
public enum Referenztyp {

    MINIKAENGURU, SERIE;
}
