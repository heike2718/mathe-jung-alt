// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Referenztyp
 */
@Schema(name = "Referenztyp", description = "Kontext zur Interpretation einer ID im alten Aufgabenarchiv")
public enum Referenztyp {

	MINIKAENGURU,
	SERIE;
}
