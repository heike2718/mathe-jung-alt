// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;

/**
 * RaetselMediensucheTrefferItem
 */
@Schema(name = "RaetselMediensucheTrefferItem", description = "Rätsel, das ein gegebenes Medium als Quelle hat.")
public class RaetselMediensucheTrefferItem {

	@JsonProperty
	@Schema(description = "technische ID des Rätsels", example = "46b8bd51-9cd6-4d06-9b5b-26495b5e5a4c")
	private String id;

	@JsonProperty
	@Schema(description = "fachlicher Schlüssel im Aufgabenarchiv.", example = "02821")
	private String schluessel;

	@JsonProperty
	@Schema(description = "kurzer Titel zum Anzeigen in Suchergebnissen, volltextsuchfähig", example = "Kängurus springen 6 Mal")
	private String name;

	@JsonProperty
	@Schema(description = "ob das Rätsel freigegeben ist.")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "Der Herkunftstyp: in diesem Kontext nur ZITAT oder ADAPTION", example = "ZITAT")
	private RaetselHerkunftTyp herkunftstyp;

	@JsonProperty
	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe", example = "alpha (6) 1976, S.32")
	private String quellenangabe;

	@JsonProperty
	@Schema(
		description = "Pfad zu einer Datei im eigenen Filesystem, in dem die Vorlage der Aufgabe oder die Aufgabe steht.",
		example = "/mathe/aufgabensammlungen/buch.pdf")
	private String pfad;

	public String getId() {

		return id;
	}

	public RaetselMediensucheTrefferItem withId(final String id) {

		this.id = id;
		return this;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public RaetselMediensucheTrefferItem withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public String getName() {

		return name;
	}

	public RaetselMediensucheTrefferItem withName(final String name) {

		this.name = name;
		return this;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public RaetselMediensucheTrefferItem withFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
		return this;
	}

	public RaetselHerkunftTyp getHerkunftstyp() {

		return herkunftstyp;
	}

	public RaetselMediensucheTrefferItem withHerkunftstyp(final RaetselHerkunftTyp herkunft) {

		this.herkunftstyp = herkunft;
		return this;
	}

	public String getQuellenangabe() {

		return quellenangabe;
	}

	public RaetselMediensucheTrefferItem withQuellenangabe(final String quellenangabe) {

		this.quellenangabe = quellenangabe;
		return this;
	}

	public String getPfad() {

		return pfad;
	}

	public RaetselMediensucheTrefferItem withPfad(final String pfad) {

		this.pfad = pfad;
		return this;
	}

}
