// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import de.egladil.mja_api.domain.semantik.ValueObject;

/**
 * Antwortvorschlag
 */
@ValueObject
public class Antwortvorschlag {

	private String buchstabe;

	private String text;

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
