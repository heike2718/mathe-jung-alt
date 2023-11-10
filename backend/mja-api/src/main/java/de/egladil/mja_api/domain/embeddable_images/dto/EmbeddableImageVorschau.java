// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EmbeddableImageVorschau
 */
@Schema(name = "EmbeddableImageVorschau", description = "Vorschau (png) einr Image-Datei, die in LaTeX eingebunden wird.")
public class EmbeddableImageVorschau {

	@JsonProperty
	@Schema(description = "der relative Pfad im LaTeX")
	private String pfad;

	@JsonProperty
	@Schema(
		description = "Flag, ob es die Datei gibt. Es kann zu Fehlern beim Generieren der Vorschau gegeben haben. Dann ist das image auch null")
	private boolean exists;

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

	public byte[] getImage() {

		return image;
	}

	public EmbeddableImageVorschau withImage(final byte[] image) {

		this.image = image;
		return this;
	}

	public boolean isExists() {

		return exists;
	}

	public EmbeddableImageVorschau markExists() {

		this.exists = true;
		return this;
	}

}
