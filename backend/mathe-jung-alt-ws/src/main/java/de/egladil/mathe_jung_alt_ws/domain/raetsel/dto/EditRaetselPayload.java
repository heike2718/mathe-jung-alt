// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

/**
 * EditRaetselPayload
 */
public class EditRaetselPayload {

	private boolean latexHistorisieren;

	private String idAendernderUser;

	private RaetselPayloadDaten daten;

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

	public RaetselPayloadDaten getDaten() {

		return daten;
	}

	public void setDaten(final RaetselPayloadDaten daten) {

		this.daten = daten;
	}

}
