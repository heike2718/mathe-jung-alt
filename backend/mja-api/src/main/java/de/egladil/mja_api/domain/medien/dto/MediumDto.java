// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * MediumDto
 */
@Schema(description = "ein Buch, eine Zeitschrift oder etwas aus dem Internet")
public class MediumDto {

	@Schema(description = "technische ID, 'neu' für neue Medien", example = "22b1a484-e9ff-43cc-a658-bd0f77e580cc")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält ungültige Zeichen")
	private String id;

	@Schema(description = "Art des Mediums", required = true)
	@NotNull(message = "medienart ist erforderlich")
	private Medienart medienart;

	@Schema(description = "Titel des Mediums", example = "2 mal 3 plus Spaß dabei", required = true)
	@NotBlank(message = "titel ist erforderlich")
	private String titel;

	@Schema(description = "Autor, bei mehreren kommaseparierte Liste", example = "Johannes Lehmann")
	@Pattern(regexp = MjaRegexps.VALID_PERSON, message = "autor enthält ungültige Zeichen")
	private String autor;

	@Schema(description = "falls vorhanden und bekannt, die Website", example = "https://mathe-jung-alt.de")
	@Pattern(regexp = MjaRegexps.VALID_URL, message = "url enthält ungültige Zeichen")
	private String url;

	@Schema(description = "Platz für Notizen, die bei der Suche berücksichtigt werden.", example = "wird nicht mehr aufgelegt")
	private String kommentar;

	@JsonProperty
	@Schema(description = "Zeigt an, ob die Person, die das Rätsel geladen hat, änderungsberechtigt ist.")
	private boolean schreibgeschuetzt = true; // erstmal immer schreibgeschuetzt. Beim Laden der Details wird entschieden, ob es
												// durch den User änderbar ist.

	@JsonProperty
	private boolean ownMedium;

	public String getId() {

		return id;
	}

	public MediumDto withId(final String id) {

		this.id = id;
		return this;
	}

	public Medienart getMedienart() {

		return medienart;
	}

	public MediumDto withMedienart(final Medienart medienart) {

		this.medienart = medienart;
		return this;
	}

	public String getTitel() {

		return titel;
	}

	public MediumDto withTitel(final String titel) {

		this.titel = titel;
		return this;
	}

	public String getAutor() {

		return autor;
	}

	public MediumDto withAutor(final String autor) {

		this.autor = autor;
		return this;
	}

	public String getUrl() {

		return url;
	}

	public MediumDto withUrl(final String url) {

		this.url = url;
		return this;
	}

	public String getKommentar() {

		return kommentar;
	}

	public MediumDto withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

	public boolean isSchreibgeschuetzt() {

		return schreibgeschuetzt;
	}

	public void markiereAlsAenderbar() {

		this.schreibgeschuetzt = false;
	}

	public boolean isOwnMedium() {

		return ownMedium;
	}

	public void setOwnMedium(final boolean owner) {

		this.ownMedium = owner;
	}

}
