// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.dto;

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

}
