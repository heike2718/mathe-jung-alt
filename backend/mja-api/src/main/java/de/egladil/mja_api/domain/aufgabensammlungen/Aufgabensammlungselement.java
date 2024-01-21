// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;

/**
 * Aufgabensammlungselement
 */
@Schema(name = "Aufgabensammlungselement", description = "Element einer Aufgabensammlung")
public class Aufgabensammlungselement {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neues Element")
	private String id;

	@JsonProperty
	@Schema(description = "Nummer als Titel der Aufgabe in der Aufgabensammlung.")
	private String nummer;

	@JsonProperty
	@Schema(description = "Punkte für dieses Rätsel, multipliziert mit 100, um Rundungsfehler zu vermeiden.")
	private int punkte;

	@JsonProperty
	@Schema(description = "fachlicher SCHLUESSEL des Rätsels, nicht Teil der persistenten Daten. Nützlich für die Anzeige")
	private String raetselSchluessel;

	@JsonProperty
	@Schema(description = "Name des Rätsels, nicht Teil der Daten des Elements. Nützlich für die Anzeige")
	private String name;

	@JsonProperty
	@Schema(description = "Loesungsbuchstabe, falls vorhanden. Kann null sein")
	private String loesungsbuchstabe;

	@JsonProperty
	@Schema(description = "Vorschautext aus Teilen der Frage")
	private String vorschautext;

	@JsonProperty
	@Schema(description = "Der Herkunftstyp: EIGENKREATION, ZITAT, ADAPTION")
	private RaetselHerkunftTyp herkunftstyp;

	@JsonProperty
	@Schema(description = "Ob das referenzierte Rätsel freigegeben ist.")
	private boolean freigegeben;

	public String getNummer() {

		return nummer;
	}

	public String getId() {

		return id;
	}

	public int getPunkte() {

		return punkte;
	}

	public String getRaetselSchluessel() {

		return raetselSchluessel;
	}

	public String getName() {

		return name;
	}

	public Aufgabensammlungselement withId(final String id) {

		this.id = id;
		return this;
	}

	public Aufgabensammlungselement withNummer(final String nummer) {

		this.nummer = nummer;
		return this;
	}

	public Aufgabensammlungselement withPunkte(final int punkte) {

		this.punkte = punkte;
		return this;
	}

	public Aufgabensammlungselement withRaetselSchluessel(final String raetselSchluessel) {

		this.raetselSchluessel = raetselSchluessel;
		return this;
	}

	public Aufgabensammlungselement withName(final String name) {

		this.name = name;
		return this;
	}

	public void setLoesungsbuchstabe(final String loesungsbuchstabe) {

		this.loesungsbuchstabe = loesungsbuchstabe;
	}

	public Aufgabensammlungselement withVorschautext(final String vorschautext) {

		this.vorschautext = vorschautext;
		return this;
	}

	public Aufgabensammlungselement withHerkunftstyp(final RaetselHerkunftTyp herkunftstyp) {

		this.herkunftstyp = herkunftstyp;
		return this;
	}

	public Aufgabensammlungselement withFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
		return this;
	}
}
