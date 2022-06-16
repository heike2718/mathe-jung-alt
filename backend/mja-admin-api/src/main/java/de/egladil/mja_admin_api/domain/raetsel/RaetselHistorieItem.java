// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel;

import java.util.Date;

import de.egladil.mja_admin_api.domain.AbstractDomainEntity;

/**
 * RaetselHistorieItem
 */
public class RaetselHistorieItem extends AbstractDomainEntity {

	private String raetselUuid;

	public String getRaetselUuid() {

		return raetselUuid;
	}

	public void setRaetselUuid(final String raetselUuid) {

		this.raetselUuid = raetselUuid;
	}

	public String getFrage() {

		return frage;
	}

	public void setFrage(final String frage) {

		this.frage = frage;
	}

	public String getLoesung() {

		return loesung;
	}

	public void setLoesung(final String loesung) {

		this.loesung = loesung;
	}

	public Date getDatum() {

		return datum;
	}

	public void setDatum(final Date datum) {

		this.datum = datum;
	}

	public String getGeaendertDurch() {

		return geaendertDurch;
	}

	public void setGeaendertDurch(final String geaendertDurch) {

		this.geaendertDurch = geaendertDurch;
	}

	private String frage;

	private String loesung;

	private Date datum;

	private String geaendertDurch;

}
