// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.raetselgruppen.impl.RaetselgruppenelementComparator;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;

/**
 * RaetselgruppeDetails
 */
@Schema(
	name = "RaetselgruppeDetails",
	description = "Details einer Rätselgruppe für die Anzeige oder zum Generieren von PDF oder LaTeX. Eine Rätselruppe ist eine freie Zusammenstellung von Rätseln. Ziel ist die Erzeugung eines PDFs. Die Rästel werden in der Reihenfolge gedruckt, in der sie in der Rätselgruppe eingetragen werden.")
public class RaetselgruppeDetails {

	@JsonProperty
	@Schema(description = "technische ID")
	private String id;

	@JsonProperty
	@Schema(description = "Name der Rätselgruppe")
	private String name;

	@JsonProperty
	@Schema(description = "optionaler Kommentar")
	private String kommentar;

	@JsonProperty
	@Schema(description = "Schwierigkeitsgrad, für das diese Rätselgruppe gedacht ist")
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
	@Schema(description = "Teil der UUID der Person, die die Rätselgruppe angelegt oder geändert hat")
	private String geaendertDurch;

	@JsonProperty
	@Schema(type = SchemaType.ARRAY, description = "Elemente der Rätselgruppe")
	private List<Raetselgruppenelement> elemente = new ArrayList<>();

	public static RaetselgruppeDetails createFromDB(final PersistenteRaetselgruppe gruppeDB) {

		RaetselgruppeDetails result = new RaetselgruppeDetails();
		result.geaendertDurch = gruppeDB.geaendertDurch;
		result.id = gruppeDB.uuid;
		result.kommentar = gruppeDB.kommentar;
		result.name = gruppeDB.name;
		result.referenz = gruppeDB.referenz;
		result.referenztyp = gruppeDB.referenztyp;
		result.schwierigkeitsgrad = gruppeDB.schwierigkeitsgrad;
		result.status = gruppeDB.status;
		return result;
	}

	public void addElement(final Raetselgruppenelement element) {

		this.elemente.add(element);
	}

	public void sortElemente() {

		Collections.sort(elemente, new RaetselgruppenelementComparator());
	}

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

	public String getGeaendertDurch() {

		return geaendertDurch;
	}

	public List<Raetselgruppenelement> getElemente() {

		return elemente;
	}
}
