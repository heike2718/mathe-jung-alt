// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * EditRaetselPayload
 */
public class EditRaetselPayload {

	private boolean latexHistorisieren;

	private String idAendernderUser;

	private Raetsel raetsel;

	public boolean isLatexHistorisieren() {

		return latexHistorisieren;
	}

	public void setLatexHistorisieren(final boolean latexHistorisieren) {

		this.latexHistorisieren = latexHistorisieren;
	}

	public String getIdAendernderUser() {

		return idAendernderUser;
	}

	public void setIdAendernderUser(final String idAendernderUser) {

		this.idAendernderUser = idAendernderUser;
	}

	public Raetsel getRaetsel() {

		return raetsel;
	}

	public void setRaetsel(final Raetsel raetsel) {

		this.raetsel = raetsel;
	}

}
