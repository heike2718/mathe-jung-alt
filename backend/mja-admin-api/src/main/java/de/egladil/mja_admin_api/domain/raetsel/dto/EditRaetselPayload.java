// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.dto;

import de.egladil.mja_admin_api.domain.raetsel.Raetsel;

/**
 * EditRaetselPayload
 */
public class EditRaetselPayload {

	private boolean latexHistorisieren;

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
