// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;

/**
 * EditRaetselgruppePayload
 */
@Schema(
	name = "EditRaetselgruppePayload",
	description = "Payload zum Anlegen und Ändern einer Rätselgruppe ohne Elemente.")
public class EditRaetselgruppePayload {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neue Rätselgruppen")
	private String id;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*")
	@Size(min = 1, max = 100)
	@NotBlank
	@Schema(description = "Name der Rätselgruppe")
	private String name;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*")
	@Size(max = 200)
	@Schema(description = "optionaler Kommentar")
	private String kommentar;

	@JsonProperty
	@NotNull
	@Schema(description = "Schwierigkeitsgrad, für den diese Rätselgruppe gedacht ist. (enum Schwierigkeitsgrad)")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@NotNull
	@Schema(description = "Refernztyp - Verbindung zum alten Aufgabenarchiv, Kontext zur Interpretation des Attributs referenz")
	private Referenztyp referenztyp;

	@JsonProperty
	@Pattern(regexp = "^[\\w äöüß]{1,20}$")
	@Size(min = 1, max = 36)
	@NotBlank
	@Schema(description = "ID einers Wettbwerbs oder einer Serie im alten Aufgabenarchiv")
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
