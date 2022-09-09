// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;

/**
 * EditRaetselgruppePayload
 */
public class EditRaetselgruppePayload {

	@JsonProperty
	private String id;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*")
	@Size(min = 1, max = 100)
	@NotBlank
	private String name;

	@JsonProperty
	@Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*")
	@Size(max = 200)
	private String kommentar;

	@JsonProperty
	@NotNull
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@NotNull
	private Referenztyp referenztyp;

	@JsonProperty
	@Pattern(regexp = "^[\\w äöüß]{1,20}$")
	@Size(min = 1, max = 36)
	@NotBlank
	private String referenz;

	@JsonProperty
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

	void setId(final String id) {

		this.id = id;
	}

	void setName(final String name) {

		this.name = name;
	}

	void setKommentar(final String kommentar) {

		this.kommentar = kommentar;
	}

	void setSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		this.schwierigkeitsgrad = schwierigkeitsgrad;
	}

	void setReferenztyp(final Referenztyp referenztyp) {

		this.referenztyp = referenztyp;
	}

	void setReferenz(final String referenz) {

		this.referenz = referenz;
	}

	void setStatus(final DomainEntityStatus status) {

		this.status = status;
	}
}
