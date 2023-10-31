// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EmbeddableImageContext
 */
@Schema(description = "Kontext der Aktion, die das Hochladen der einzubettenden Grafik enthält.")
public class EmbeddableImageContext {

	@JsonProperty
	@Schema(description = "transportiert die raetselId aus der Payoad unverändert zurück zum Aufrufer.")
	private String raetselId;

	@JsonProperty
	@Schema(description = "transportiert die textart aus der Payoad unverändert zurück zum Aufrufer.")
	private Textart textart;

	public String getRaetselId() {

		return raetselId;
	}

	public EmbeddableImageContext withRaetselId(final String raetselId) {

		this.raetselId = raetselId;
		return this;
	}

	public Textart getTextart() {

		return textart;
	}

	public EmbeddableImageContext withTextart(final Textart textart) {

		this.textart = textart;
		return this;
	}
}
