// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;

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
	@Schema(description = "poptionaler Kommentar")
	private String kommentar;

	@JsonIgnore
	private Schwierigkeitsgrad schwierigkeitsgradType;

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
}
