// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EmbeddableImageInfo
 */
@Schema(
	name = "EmbeddableImageInfo",
	description = "Info über ein im LaTeX-Code der Frage oder Lösung eingebundenes eps-Image.")
public class EmbeddableImageInfo {

	@JsonProperty
	@Schema(description = "Pfad des eps relativ zum LaTex-File im LaTeX-Code")
	private String pfad;

	@JsonProperty
	@Schema(description = "Flag, ob das File schon hochgeladen wurde. Erst dann lässt sich das Rätsel generieren.")
	private boolean existiert;

	/**
	 *
	 */
	public EmbeddableImageInfo() {

		super();

	}

	/**
	 * @param pfad
	 * @param existiert
	 */
	public EmbeddableImageInfo(final String pfad, final boolean existiert) {

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
