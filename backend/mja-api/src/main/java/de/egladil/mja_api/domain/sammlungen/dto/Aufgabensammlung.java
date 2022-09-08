// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.sammlungen.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Aufgabensammlung
 */
public class Aufgabensammlung {

	@JsonProperty
	private String name;

	@JsonProperty
	private String klassenstufe;

	@JsonProperty
	private List<Aufgabe> aufgaben;

	public String getName() {

		return name;
	}

	public Aufgabensammlung withName(final String name) {

		this.name = name;
		return this;
	}

	public String getKlassenstufe() {

		return klassenstufe;
	}

	public Aufgabensammlung withKlassenstufe(final String klassenstufe) {

		this.klassenstufe = klassenstufe;
		return this;
	}

	public List<Aufgabe> getAufgaben() {

		return aufgaben;
	}

	public void setAufgaben(final List<Aufgabe> aufgaben) {

		this.aufgaben = aufgaben;
	}

}
