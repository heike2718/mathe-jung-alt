// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * EditRaetselgruppePayload
 */
@Schema(
	name = "EditRaetselgruppePayload",
	description = "Payload zum Anlegen und Ändern einer Rätselgruppe ohne Elemente.")
public class EditRaetselgruppePayload {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neue Rätselgruppen")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält ungültige Zeichen")
	private String id;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*", message = "name enthält ungültige Zeichen")
	@Size(min = 1, max = 100, message = "name darf nicht länger als 100 Zeichen sein")
	@NotBlank(message = "name darf nicht leer sein")
	@Schema(description = "Name der Rätselgruppe", example = "Serie 42")
	private String name;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*", message = "kommentar enthält ungültige Zeichen")
	@Size(max = 200, message = "kommentar darf nicht länger als 200 Zeichen sein")
	@Schema(description = "optionaler Kommentar", example = "Aufgaben der Serie 42")
	private String kommentar;

	@JsonProperty
	@NotNull(message = "schwierigkeitsgrad darf nicht null sein")
	@Schema(description = "Schwierigkeitsgrad, für den diese Rätselgruppe gedacht ist. (enum Schwierigkeitsgrad)")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@Schema(description = "Refernztyp - Verbindung zum alten Aufgabenarchiv, Kontext zur Interpretation des Attributs referenz")
	private Referenztyp referenztyp;

	@JsonProperty
	@Pattern(regexp = "^[\\w äöüß]{1,20}$", message = "referenz enthält ungültige Zeichen")
	@Size(min = 1, max = 36, message = "referenz darf nicht länger als 36 Zeichen sein")
	@Schema(description = "ID einers Wettbwerbs oder einer Serie im alten Aufgabenarchiv", example = "2020")
	private String referenz;

	@JsonProperty
	@Schema(description = "Veröffentlichungsstatus der Rätselgruppe. Nur freigegebene sind über die Open-Data-API abrufbar")
	private DomainEntityStatus status;

	public String getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public String getKommentar() {

		return kommentar;
	}

	public Schwierigkeitsgrad getSchwierigkeitsgrad() {

		return schwierigkeitsgrad;
	}

	public Referenztyp getReferenztyp() {

		return referenztyp;
	}

	public String getReferenz() {

		return referenz;
	}

	public DomainEntityStatus getStatus() {

		return status;
	}

	public void setId(final String id) {

		this.id = id;
	}

	public void setName(final String name) {

		this.name = name;
	}

	public void setKommentar(final String kommentar) {

		this.kommentar = kommentar;
	}

	public void setSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		this.schwierigkeitsgrad = schwierigkeitsgrad;
	}

	public void setReferenztyp(final Referenztyp referenztyp) {

		this.referenztyp = referenztyp;
	}

	public void setReferenz(final String referenz) {

		this.referenz = referenz;
	}

	public void setStatus(final DomainEntityStatus status) {

		this.status = status;
	}
}
