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
 * QuelleMinimalDto ist das, was im Rätseleditor angezeigt wird (ID und Name). Mit jedem Rätel wird es nachgeladen
 */
public class QuelleMinimalDto {

	@JsonProperty
	@Schema(description = "technische ID der Quelle")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "quelleID enthält ungültige Zeichen - muss eine UUID sein")
	private String id;

	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe")
	private String name;

	public String getId() {

		return id;
	}

	public QuelleMinimalDto withId(final String id) {

		this.id = id;
		return this;
	}

	public String getName() {

		return name;
	}

	public QuelleMinimalDto withName(final String name) {

		this.name = name;
		return this;
	}

}
