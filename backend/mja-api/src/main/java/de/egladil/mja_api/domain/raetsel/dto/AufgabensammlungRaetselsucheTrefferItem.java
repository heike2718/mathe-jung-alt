// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;

/**
 * AufgabensammlungRaetselsucheTrefferItem
 */
@Schema(
	name = "AufgabensammlungSucheTrefferItem",
	description = "Ein Element einer Suche nach Aufgabensammlungen, in denen ein Rätsel vorkommt.")
public class AufgabensammlungRaetselsucheTrefferItem {

	@JsonProperty
	@Schema(description = "technische ID")
	private String id;

	@JsonProperty
	@Schema(description = "Name der Aufgabensammlung")
	private String name;

	@JsonProperty
	@Schema(description = "Nummer, die das Rätsel in dieser Aufgabensammlung hat.")
	private String nummer;

	@JsonProperty
	@Schema(description = "Punkte für dieses Rätsel, multipliziert mit 100, um Rundungsfehler zu vermeiden.")
	private int punkte;

	@JsonProperty
	@Schema(name = "schwierigkeitsgrad", description = "Klassenstufe, für die die Aufgabensammlung gedacht ist")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung freigegeben ist.")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung privat ist, also keinem Autor gehört.")
	private boolean privat;

	@JsonProperty
	@Schema(description = "abgekürzte UUID des Owners")
	private String owner;

	public String getId() {

		return id;
	}

	public AufgabensammlungRaetselsucheTrefferItem withId(final String id) {

		this.id = id;
		return this;
	}

	public String getName() {

		return name;
	}

	public AufgabensammlungRaetselsucheTrefferItem withName(final String name) {

		this.name = name;
		return this;
	}

	public Schwierigkeitsgrad getSchwierigkeitsgrad() {

		return schwierigkeitsgrad;
	}

	public AufgabensammlungRaetselsucheTrefferItem withSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		this.schwierigkeitsgrad = schwierigkeitsgrad;
		return this;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public AufgabensammlungRaetselsucheTrefferItem withFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
		return this;
	}

	public boolean isPrivat() {

		return privat;
	}

	public AufgabensammlungRaetselsucheTrefferItem withPrivat(final boolean privat) {

		this.privat = privat;
		return this;
	}

	public String getNummer() {

		return nummer;
	}

	public AufgabensammlungRaetselsucheTrefferItem withNummer(final String nummer) {

		this.nummer = nummer;
		return this;
	}

	public int getPunkte() {

		return punkte;
	}

	public AufgabensammlungRaetselsucheTrefferItem withPunkte(final int punkte) {

		this.punkte = punkte;
		return this;
	}

	public String getOwner() {

		return owner;
	}

	public AufgabensammlungRaetselsucheTrefferItem withOwner(final String owner) {

		this.owner = owner;
		return this;
	}
}
