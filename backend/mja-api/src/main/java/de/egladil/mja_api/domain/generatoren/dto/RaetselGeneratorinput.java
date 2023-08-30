// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.dto;

import java.util.Arrays;
import java.util.Optional;

import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;

/**
 * RaetselGeneratorinput
 */
public class RaetselGeneratorinput {

	private String nummer;

	private String schluessel;

	private String frage;

	private String loesung;

	private int punkte;

	private Antwortvorschlag[] antwortvorschlaege;

	private LayoutAntwortvorschlaege layoutAntwortvorschlaege;

	private Verwendungszweck verwendungszweck;

	public String getTextColor() {

		switch (punkte) {

		case 300:
			return "green";

		case 400:
			return "blue";

		case 500:
			return "orange";

		default:
			return "black";
		}
	}

	public String getLoesungsbuchstabe() {

		if (antwortvorschlaege == null) {

			return "";
		}

		Optional<Antwortvorschlag> optKorrekt = Arrays.stream(antwortvorschlaege).filter(v -> v.isKorrekt()).findFirst();

		return optKorrekt.isEmpty() ? "" : optKorrekt.get().getBuchstabe();

	}

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

	public int getPunkte() {

		return punkte;
	}

	public RaetselGeneratorinput withPunkten(final int punkte) {

		this.punkte = punkte;
		return this;
	}

	public String getNummer() {

		return nummer;
	}

	public RaetselGeneratorinput withNummer(final String nummer) {

		this.nummer = nummer;
		return this;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public RaetselGeneratorinput withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public Verwendungszweck getVerwendungszweck() {

		return verwendungszweck;
	}

	public RaetselGeneratorinput withVerwendungszweck(final Verwendungszweck verwendungszweck) {

		this.verwendungszweck = verwendungszweck;
		return this;
	}
}
