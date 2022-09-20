// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raetselgruppenelement
 */
@Schema(name = "Raetselgruppenelement", description = "Element einer Rätselgruppe")
public class Raetselgruppenelement {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neues Element")
	private String id;

	@JsonProperty
	@Schema(description = "Nummer, die die Reihenfolge innerhalb der Gruppe festlegt")
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

	public String getNummer() {

		return nummer;
	}

	public void setNummer(final String nummer) {

		this.nummer = nummer;
	}

	public int getPunkte() {

		return punkte;
	}

	public void setPunkte(final int punkte) {

		this.punkte = punkte;
	}

	public String getRaetselSchluessel() {

		return raetselSchluessel;
	}

	public void setRaetselSchluessel(final String raetselSchluessel) {

		this.raetselSchluessel = raetselSchluessel;
	}

	public String getName() {

		return name;
	}

	public void setName(final String name) {

		this.name = name;
	}

}
