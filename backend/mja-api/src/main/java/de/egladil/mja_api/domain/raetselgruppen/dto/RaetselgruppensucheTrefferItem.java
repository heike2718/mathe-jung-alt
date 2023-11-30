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

/**
 * RaetselgruppensucheTrefferItem
 */
public class RaetselgruppensucheTrefferItem {

	@JsonProperty
	@Schema(description = "technische ID")
	private String id;

	@JsonProperty
	@Schema(description = "Name der Rätselgruppe")
	private String name;

	@JsonProperty
	@Schema(description = "poptionaler Kommentar")
	private String kommentar;

	@JsonProperty
	@Schema(name = "schwierigkeitsgrad", description = "Klassenstufe, für die die Rätselgruppe gedacht ist")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@Schema(description = "Refernztyp - Verbindung zum alten Aufgabenarchiv, Kontext zur Interpretation des Attributs referenz")
	private Referenztyp referenztyp;

	@JsonProperty
	@Schema(description = "ID einers Wettbwerbs oder einer Serie im alten Aufgabenarchiv")
	private String referenz;

	@JsonProperty
	@Schema(description = "Veröffentlichungsstatus der Rätselgruppe. Nur freigegebene sind über die Open-Data-API abrufbar")
	private DomainEntityStatus status;

	@JsonProperty
	@Schema(description = "ID des Änderers")
	private String geaendertDurch;

	@JsonProperty
	@Schema(description = "Anzahl der Elemente (also der Rätsel)")
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

	@Override
	public String toString() {

		return super.toString();
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

	public String getGeaendertDurch() {

		return geaendertDurch;
	}

	public void setGeaendertDurch(final String geaendertDurch) {

		this.geaendertDurch = geaendertDurch;
	}

}
