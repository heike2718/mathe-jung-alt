// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * QuellenangabeRaetsel ist das, was im Rätseleditor angezeigt wird (ID und Name). Mit jedem Rätel wird es nachgeladen
 */
public class QuellenangabeRaetsel {

	@JsonProperty
	@Schema(description = "technische ID der Quelle")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "quelleID enthält ungültige Zeichen - muss eine UUID sein")
	private String id;

	@Schema(description = "Art der Quelle: Mensch, Buch, Zeitschrift")
	private Quellenart quellenart;

	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe")
	private String name;

	@Schema(description = "optional Referenz auf ein Buch, eine Zeitschrift oder etwas im Internet")
	private String mediumUuid;

	public String getId() {

		return id;
	}

	public QuellenangabeRaetsel withId(final String id) {

		this.id = id;
		return this;
	}

	public String getName() {

		return name;
	}

	public QuellenangabeRaetsel withName(final String name) {

		this.name = name;
		return this;
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public QuellenangabeRaetsel withQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
		return this;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public QuellenangabeRaetsel withMediumUuid(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
		return this;
	}

}
