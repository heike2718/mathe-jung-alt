// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.embeddable_images.dto.Textart;

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

	@JsonProperty
	@Schema(description = "In welchem Text des Rätsels ist das Bild eingebunden: FRAGE | LOESUNG")
	private Textart textart;

	/**
	 *
	 */
	EmbeddableImageInfo() {

	}

	/**
	 * @param pfad
	 * @param existiert
	 */
	public EmbeddableImageInfo(final String pfad, final boolean existiert, final Textart textart) {

		super();
		this.pfad = pfad;
		this.existiert = existiert;
		this.textart = textart;
	}

	public String getPfad() {

		return pfad;
	}

	public boolean isExistiert() {

		return existiert;
	}

	@JsonIgnore
	public String getFilename() {

		String[] tokens = StringUtils.split(pfad, '/');
		return tokens[tokens.length - 1];

	}
}
