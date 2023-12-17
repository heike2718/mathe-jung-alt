// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * HerkunftRaetsel gibt Auskunft über die Herkunft des Rätsels.
 */
public class HerkunftRaetsel {

	@JsonProperty
	@Schema(description = "technische ID der Quelle", example = "b26aecdd-628a-4b85-b761-b32abf82edc1")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "quelleID enthält ungültige Zeichen - muss eine UUID sein")
	private String id;

	@Schema(description = "Art der Quelle: Mensch, Buch, Zeitschrift")
	private Quellenart quellenart;

	@Schema(description = "Der Herkunftstyp: EIGENKREATION, ZITAT, ADAPTION")
	private RaetselHerkunftTyp herkunftstyp;

	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe", example = "alpha (6) 1976, S.32")
	private String text;

	@Schema(
		description = "optional Referenz auf ein Buch, eine Zeitschrift oder etwas im Internet",
		example = "343d9c5f-68e1-4dd9-98cc-1eea3493a559")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "mediumUuid enthält ungültige Zeichen - muss eine UUID sein")
	private String mediumUuid;

	public String getId() {

		return id;
	}

	public HerkunftRaetsel withId(final String id) {

		this.id = id;
		return this;
	}

	public String getText() {

		return text;
	}

	public HerkunftRaetsel withText(final String text) {

		this.text = text;
		return this;
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public HerkunftRaetsel withQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
		return this;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public HerkunftRaetsel withMediumUuid(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
		return this;
	}

	public RaetselHerkunftTyp getHerkunftstyp() {

		return herkunftstyp;
	}

	public HerkunftRaetsel withHerkunftstyp(final RaetselHerkunftTyp herkunftstyp) {

		this.herkunftstyp = herkunftstyp;
		return this;
	}

}
