// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Images
 */
@Schema(
	name = "Images",
	description = "Objekt das ein png mit dem Text für die Frage/Quizaufgabe und ein png mit dem Text für die Lösung enthält")
public class Images {

	@JsonProperty
	@Schema(
		description = "Das png mit Maßen für die Frage")
	private MjaImage imageFrage;

	@JsonProperty
	@Schema(
		description = "Das png mit Maßen für die Lösung. Es kann null sein.")
	private MjaImage imageLoesung;

	public MjaImage getImageFrage() {

		return imageFrage;
	}

	public Images withImageFrage(final MjaImage imageFrage) {

		this.imageFrage = imageFrage;
		return this;
	}

	public MjaImage getImageLoesung() {

		return imageLoesung;
	}

	public Images withImageLoesung(final MjaImage imageLoesung) {

		this.imageLoesung = imageLoesung;
		return this;
	}
}
