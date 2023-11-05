// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * EmbeddableImageContext
 */
@Schema(description = "Kontext der Aktion, die das Hochladen der einzubettenden EmbeddableImageVorschau enthält.")
public class EmbeddableImageContext {

	@JsonProperty
	@Schema(
		description = "transportiert die raetselId aus der Payload unverändert zurück zum Aufrufer. Wenn nicht 'neu', muss das zugehörige Rätsel vorhanden sein.")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "die raetselId enthält ungültige Zeichen")
	private String raetselId;

	@JsonProperty
	@Schema(description = "transportiert die textart aus der Payload unverändert zurück zum Aufrufer.")
	private Textart textart;

	@JsonProperty
	private boolean replaced = false;

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((raetselId == null) ? 0 : raetselId.hashCode());
		result = prime * result + ((textart == null) ? 0 : textart.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (!(obj instanceof EmbeddableImageContext)) {

			return false;
		}
		EmbeddableImageContext other = (EmbeddableImageContext) obj;

		if (raetselId == null) {

			if (other.raetselId != null) {

				return false;
			}
		} else if (!raetselId.equals(other.raetselId)) {

			return false;
		}

		if (textart != other.textart) {

			return false;
		}
		return true;
	}

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

	public boolean isReplaced() {

		return replaced;
	}

	public EmbeddableImageContext withReplaced(final boolean replaced) {

		this.replaced = replaced;
		return this;
	}
}
