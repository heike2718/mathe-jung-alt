// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GrafikInfo
 */
public class GrafikInfo {

	@JsonProperty
	private String pfad;

	@JsonProperty
	private boolean existiert;

	/**
	 *
	 */
	public GrafikInfo() {

		super();

	}

	/**
	 * @param pfad
	 * @param existiert
	 */
	public GrafikInfo(final String pfad, final boolean existiert) {

		super();
		this.pfad = pfad;
		this.existiert = existiert;
	}

	public String getPfad() {

		return pfad;
	}

	public void setPfad(final String pfad) {

		this.pfad = pfad;
	}

	public boolean isExistiert() {

		return existiert;
	}

	public void setExistiert(final boolean vorhanden) {

		this.existiert = vorhanden;
	}

}
