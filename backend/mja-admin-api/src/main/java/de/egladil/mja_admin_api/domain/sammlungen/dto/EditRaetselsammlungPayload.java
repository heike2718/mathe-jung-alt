// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_admin_api.domain.DomainEntityStatus;
import de.egladil.mja_admin_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_admin_api.domain.sammlungen.Schwierigkeitsgrad;

/**
 * EditRaetselsammlungPayload
 */
public class EditRaetselsammlungPayload {

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
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	private Referenztyp referenztyp;

	@JsonProperty
	@Pattern(regexp = "^[\\w äöüß]{1,20}$")
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
}
