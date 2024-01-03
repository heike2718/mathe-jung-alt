// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * MediensucheTrefferItem
 */
public class MediensucheTrefferItem {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neue Medien")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält ungültige Zeichen")
	private String id;

	@JsonProperty
	@Schema(description = "Art des Mediums")
	private Medienart medienart;

	@JsonProperty
	@Schema(description = "Titel des Mediums")
	private String titel;

	@JsonProperty
	@Schema(description = "Kommentar des Mediums")
	private String kommentar;

	public String getId() {

		return id;
	}

	public MediensucheTrefferItem withId(final String id) {

		this.id = id;
		return this;
	}

	public Medienart getMedienart() {

		return medienart;
	}

	public MediensucheTrefferItem withMedienart(final Medienart medienart) {

		this.medienart = medienart;
		return this;
	}

	public String getTitel() {

		return titel;
	}

	public MediensucheTrefferItem withTitel(final String titel) {

		this.titel = titel;
		return this;
	}

	public String getKommentar() {

		return kommentar;
	}

	public MediensucheTrefferItem withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

}
