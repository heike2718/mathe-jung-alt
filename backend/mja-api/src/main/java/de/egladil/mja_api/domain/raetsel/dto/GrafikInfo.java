// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GrafikInfo
 */
@Schema(
	name = "GrafikInfo",
	description = "Info über ein im LaTeX-Code der Frage oder Lösung eingebundenes eps-Image.")
public class GrafikInfo {

	@JsonProperty
	@Schema(description = "Pfad des eps relativ zum LaTex-File im LaTeX-Code")
	private String pfad;

	@JsonProperty
	@Schema(description = "Flag, ob die Grafik schon hochgeladen wurde. Erst dann lässt sich das Rätsel generieren.")
	private boolean existiert;

	/**
	 *
	 */
	public GrafikInfo() {

		super();

	}

	/**
	 * @param pfad
	 * @param existiert
	 */
	public GrafikInfo(final String pfad, final boolean existiert) {

		super();
		this.pfad = pfad;
		this.existiert = existiert;
	}

	public String getPfad() {

		return pfad;
	}

	public void setPfad(final String pfad) {

		this.pfad = pfad;
	}

	public boolean isExistiert() {

		return existiert;
	}

	public void setExistiert(final boolean vorhanden) {

		this.existiert = vorhanden;
	}

}
