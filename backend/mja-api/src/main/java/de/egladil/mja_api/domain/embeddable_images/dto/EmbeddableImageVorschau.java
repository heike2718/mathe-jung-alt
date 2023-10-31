// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;

/**
 * EmbeddableImageVorschau
 */
@Schema(name = "EmbeddableImageVorschau", description = "Vorschau (png) einr Image-Datei, die in LaTeX eingebunden wird.")
public class EmbeddableImageVorschau {

	@JsonProperty
	@Schema(description = "Info, ob Image-Datei bereits da ist")
	private MessagePayload messagePayload;

	@JsonProperty
	@Schema(description = "der relative Pfad im LaTeX")
	private String pfad;

	@JsonProperty
	@Schema(
		description = "Base64-encodetes png Kann mit einem img src=\\\"data:image/png;base64- Tag angezeigt werden. Kann null sein!")
	private byte[] image;

	public String getPfad() {

		return pfad;
	}

	public EmbeddableImageVorschau withPfad(final String pfad) {

		this.pfad = pfad;
		return this;
	}

	public MessagePayload getMessagePayload() {

		return messagePayload;
	}

	public EmbeddableImageVorschau withMessagePayload(final MessagePayload messagePayload) {

		this.messagePayload = messagePayload;
		return this;
	}

	public byte[] getImage() {

		return image;
	}

	public EmbeddableImageVorschau withImage(final byte[] image) {

		this.image = image;
		return this;
	}

}
