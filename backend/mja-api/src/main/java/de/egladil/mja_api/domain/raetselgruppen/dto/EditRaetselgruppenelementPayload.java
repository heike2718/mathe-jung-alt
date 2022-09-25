// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EditRaetselgruppenelementPayload
 */
@Schema(description = "Daten zum Anlegen und Ändern eines Rätselgruppenelements")
public class EditRaetselgruppenelementPayload {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neues Element")
	@Pattern(
		regexp = "^[neua-fA-F\\d\\-]{1,36}$",
		message = "ID enthält ungültige Zeichen - muss eine UUID sein oder neu")
	private String id;

	@JsonProperty
	@Schema(description = "Nummer, die die Reihenfolge innerhalb der Gruppe festlegt")
	@Pattern(regexp = "^[a-zA-Z\\d\\-]{1,100}$", message = "Nur a-z,A-Z, Ziffern und Minus, höchstens 100")
	@NotBlank
	private String nummer;

	@JsonProperty
	@Schema(description = "Punkte für dieses Rätsel, multipliziert mit 100, um Rundungsfehler zu vermeiden.")
	private int punkte;

	@JsonProperty
	@Schema(description = "fachlicher SCHLUESSEL des Rätsels, nicht Teil der persistenten Daten. Nützlich für die Anzeige")
	@Pattern(regexp = "^[\\d]{5}$", message = "schluessel muss aus genau 5 Ziffern bestehen")
	private String raetselSchluessel;

	public String getId() {

		return id;
	}

	public void setId(final String id) {

		this.id = id;
	}

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

}
