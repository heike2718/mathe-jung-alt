// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;

/**
 * AufgabensammlungSucheTrefferItem
 */
@Schema(name = "AufgabensammlungSucheTrefferItem", description = "Ein Element einer Suchanfrage")
public class AufgabensammlungSucheTrefferItem {

	@JsonProperty
	@Schema(description = "technische ID")
	private String id;

	@JsonProperty
	@Schema(description = "Name der Aufgabensammlung")
	private String name;

	@JsonProperty
	@Schema(description = "optionaler Kommentar")
	private String kommentar;

	@JsonProperty
	@Schema(name = "schwierigkeitsgrad", description = "Klassenstufe, für die die Aufgabensammlung gedacht ist")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@Schema(description = "Refernztyp - Verbindung zum alten Aufgabenarchiv, Kontext zur Interpretation des Attributs referenz")
	private Referenztyp referenztyp;

	@JsonProperty
	@Schema(description = "ID einers Wettbwerbs oder einer Serie im alten Aufgabenarchiv")
	private String referenz;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung freigegeben ist. Nur freigegebene sind über die Open-Data-API abrufbar")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung privat ist, also keinem Autor gehört.")
	private boolean privat;

	@JsonProperty
	@Schema(description = "ID des Änderers")
	private String geaendertDurch;

	@JsonProperty
	@Schema(description = "Anzahl der Elemente (also der Rätsel)")
	private long anzahlElemente;

	public String getId() {

		return id;
	}

	public void setId(final String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	@Override
	public String toString() {

		return super.toString();
	}

	public void setName(final String name) {

		this.name = name;
	}

	public String getKommentar() {

		return kommentar;
	}

	public void setKommentar(final String kommentar) {

		this.kommentar = kommentar;
	}

	public Schwierigkeitsgrad getSchwierigkeitsgrad() {

		return schwierigkeitsgrad;
	}

	public void setSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		this.schwierigkeitsgrad = schwierigkeitsgrad;
	}

	public Referenztyp getReferenztyp() {

		return referenztyp;
	}

	public void setReferenztyp(final Referenztyp referenztyp) {

		this.referenztyp = referenztyp;
	}

	public String getReferenz() {

		return referenz;
	}

	public void setReferenz(final String referenz) {

		this.referenz = referenz;
	}

	public long getAnzahlElemente() {

		return anzahlElemente;
	}

	public void setAnzahlElemente(final long anzahlElemente) {

		this.anzahlElemente = anzahlElemente;
	}

	public String getGeaendertDurch() {

		return geaendertDurch;
	}

	public void setGeaendertDurch(final String geaendertDurch) {

		this.geaendertDurch = geaendertDurch;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public void setFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
	}

	public boolean isPrivat() {

		return privat;
	}

	public void setPrivat(final boolean privat) {

		this.privat = privat;
	}

}
