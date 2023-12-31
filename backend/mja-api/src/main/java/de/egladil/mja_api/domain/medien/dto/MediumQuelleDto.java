// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * MediumQuelleDto
 */
@Schema(description = "Daten eines Buchs, einer Zeitschrift oder etwas aus dem Internet zur Referenzierung in einer Rätselquelle")
public class MediumQuelleDto {

	@Schema(description = "technische ID, 'neu' für neue Medien", example = "22b1a484-e9ff-43cc-a658-bd0f77e580cc")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält ungültige Zeichen")
	private String id;

	@Schema(description = "Art des Mediums", required = true)
	@NotNull(message = "medienart ist erforderlich")
	private Medienart medienart;

	@Schema(description = "Titel des Mediums", example = "2 mal 3 plus Spaß dabei", required = true)
	@NotBlank(message = "titel ist erforderlich")
	private String titel;

	public String getId() {

		return id;
	}

	public MediumQuelleDto withId(final String id) {

		this.id = id;
		return this;
	}

	public Medienart getMedienart() {

		return medienart;
	}

	public MediumQuelleDto withMedienart(final Medienart medienart) {

		this.medienart = medienart;
		return this;
	}

	public String getTitel() {

		return titel;
	}

	public MediumQuelleDto withTitel(final String titel) {

		this.titel = titel;
		return this;
	}

}
