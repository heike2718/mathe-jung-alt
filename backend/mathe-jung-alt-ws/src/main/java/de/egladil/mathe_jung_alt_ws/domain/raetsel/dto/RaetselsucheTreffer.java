// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * RaetselsucheTreffer
 */
public class RaetselsucheTreffer {

	@JsonProperty
	private String id;

	@JsonProperty
	private String schluessel;

	@JsonProperty
	private String name;

	@JsonProperty
	private List<Deskriptor> deskriptoren;

	public String getId() {

		return id;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public String getName() {

		return name;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public RaetselsucheTreffer withId(final String id) {

		this.id = id;
		return this;
	}

	public RaetselsucheTreffer withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public RaetselsucheTreffer withName(final String name) {

		this.name = name;
		return this;
	}

	public RaetselsucheTreffer withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

}
