// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.sammlungen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_api.domain.sammlungen.Schwierigkeitsgrad;

/**
 * RaetselsammlungSucheTrefferItem
 */
public class RaetselsammlungSucheTrefferItem {

	@JsonProperty
	private String id;

	@JsonProperty
	private String name;

	@JsonProperty
	private String kommentar;

	@JsonProperty
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	private Referenztyp referenztyp;

	@JsonProperty
	private String referenz;

	@JsonProperty
	private DomainEntityStatus status;

	@JsonProperty
	private long anzahlElemente;

	public String getId() {

		return id;
	}

	public void setId(final String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(final String name) {

		this.name = name;
	}

	public String getKommentar() {

		return kommentar;
	}

	public void setKommentar(final String kommentar) {

		this.kommentar = kommentar;
	}

	public Schwierigkeitsgrad getSchwierigkeitsgrad() {

		return schwierigkeitsgrad;
	}

	public void setSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		this.schwierigkeitsgrad = schwierigkeitsgrad;
	}

	public Referenztyp getReferenztyp() {

		return referenztyp;
	}

	public void setReferenztyp(final Referenztyp referenztyp) {

		this.referenztyp = referenztyp;
	}

	public String getReferenz() {

		return referenz;
	}

	public void setReferenz(final String referenz) {

		this.referenz = referenz;
	}

	public DomainEntityStatus getStatus() {

		return status;
	}

	public void setStatus(final DomainEntityStatus status) {

		this.status = status;
	}

	public long getAnzahlElemente() {

		return anzahlElemente;
	}

	public void setAnzahlElemente(final long anzahlElemente) {

		this.anzahlElemente = anzahlElemente;
	}

}
