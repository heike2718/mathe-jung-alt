// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.raetsel.Raetsel;

/**
 * EditRaetselPayload
 */
@Schema(name = "EditRaetselPayload", description = "Payload zum Anlegen und Ändern eines Rätsels")
public class EditRaetselPayload {

	@Schema(
		description = "Flag, ob Änderungen im LaTeX-Code historisiert werden sollen. Ist nur bei inhaltlichen Änderungen sinnvoll.")
	private boolean latexHistorisieren;

	@Schema(description = "die zu speichernden Daten des Rätsels")
	private Raetsel raetsel;

	public boolean isLatexHistorisieren() {

		return latexHistorisieren;
	}

	public void setLatexHistorisieren(final boolean latexHistorisieren) {

		this.latexHistorisieren = latexHistorisieren;
	}

	public Raetsel getRaetsel() {

		return raetsel;
	}

	public void setRaetsel(final Raetsel raetsel) {

		this.raetsel = raetsel;
	}

}
