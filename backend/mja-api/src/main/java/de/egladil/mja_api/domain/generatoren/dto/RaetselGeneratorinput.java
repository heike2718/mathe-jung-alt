// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.dto;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;

/**
 * RaetselGeneratorinput
 */
public class RaetselGeneratorinput {

	private String frage;

	private String loesung;

	private Antwortvorschlag[] antwortvorschlaege;

	private LayoutAntwortvorschlaege layoutAntwortvorschlaege;

	private boolean zweiseitig = false;

	public String getFrage() {

		return frage;
	}

	public RaetselGeneratorinput withFrage(final String frage) {

		this.frage = frage;
		return this;
	}

	public String getLoesung() {

		return loesung;
	}

	public RaetselGeneratorinput withLoesung(final String loesung) {

		this.loesung = loesung;
		return this;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public RaetselGeneratorinput withAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
		return this;
	}

	public LayoutAntwortvorschlaege getLayoutAntwortvorschlaege() {

		return layoutAntwortvorschlaege;
	}

	public RaetselGeneratorinput withLayoutAntwortvorschlaege(final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		this.layoutAntwortvorschlaege = layoutAntwortvorschlaege;
		return this;
	}

	public boolean isZweiseitig() {

		return zweiseitig;
	}

	public RaetselGeneratorinput withZweiseitig(final boolean zweiseitig) {

		this.zweiseitig = zweiseitig;
		return this;
	}
}
