// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.semantik.ValueObject;

/**
 * Antwortvorschlag
 */
@ValueObject
@Schema(name = "Antwortvorschlag", description = "Antwortvorschlag für ein multiple choice-Rätsel")
public class Antwortvorschlag {

	@Schema(description = "der Antwortbuchstabe A, B, C, ... zur Anzeige einer Vorschlagsauswahl")
	private String buchstabe;

	@Schema(description = "optionaler Text der Antwort")
	private String text;

	@Schema(description = "Flag, ob dies die korrekte Antwort ist.")
	private boolean korrekt;

	public String getBuchstabe() {

		return buchstabe;
	}

	public Antwortvorschlag withBuchstabe(final String buchstabe) {

		this.buchstabe = buchstabe;
		return this;
	}

	public String getText() {

		return text;
	}

	public Antwortvorschlag withText(final String text) {

		this.text = text;
		return this;
	}

	public boolean isKorrekt() {

		return korrekt;
	}

	public Antwortvorschlag withKorrekt(final boolean korrekt) {

		this.korrekt = korrekt;
		return this;
	}

}
