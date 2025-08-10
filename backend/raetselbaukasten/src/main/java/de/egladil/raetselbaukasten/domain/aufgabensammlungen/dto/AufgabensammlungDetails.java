// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.aufgabensammlungen.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Aufgabensammlungselement;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Referenztyp;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteAufgabensammlung;

/**
 * AufgabensammlungDetails
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
	name = "AufgabensammlungDetails",
	description = "Details einer Aufgabensammlung für die Anzeige oder zum Generieren von PDF oder LaTeX. Eine Rätselruppe ist eine freie Zusammenstellung von Rätseln. Ziel ist die Erzeugung eines PDFs. Die Rästel werden in der Reihenfolge gedruckt, in der sie in der Aufgabensammlung eingetragen werden.")
public class AufgabensammlungDetails {

	@JsonProperty
	@Schema(description = "technische ID", example = "1909f308-ee3e-4ea5-be02-77e30def99a5")
	private String id;

	@JsonProperty
	@Schema(description = "Name der Aufgabensammlung")
	private String name;

	@JsonProperty
	@Schema(description = "optionaler Kommentar")
	private String kommentar;

	@JsonProperty
	@Schema(description = "Schwierigkeitsgrad, für das diese Aufgabensammlung gedacht ist")
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	@Schema(description = "Refernztyp - Verbindung zum alten Aufgabenarchiv, Kontext zur Interpretation des Attributs referenz")
	private Referenztyp referenztyp;

	@JsonProperty
	@Schema(description = "ID einers Wettbwerbs oder einer Serie im alten Aufgabenarchiv")
	private String referenz;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung freigegeben ist. Nur freigegebene sind über die Open-Data-API abrufbar")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "Ob die Aufgabensammlung privat ist, also keinem Autor gehört.")
	private boolean privat;

	@JsonProperty
	@Schema(description = "Zeigt an, ob die Person, die das Rätsel geladen hat, änderungsberechtigt ist.")
	private boolean schreibgeschuetzt = true; // erstmal immer schreibgeschuetzt. Beim Laden der Details wird entschieden, ob es
												// durch den User
	// änderbar ist.

	@JsonProperty
	@Schema(description = "Teil der UUID der Person, die die Aufgabensammlung angelegt oder geändert hat")
	private String geaendertDurch;

	@JsonProperty
	@Schema(type = SchemaType.ARRAY, description = "Elemente der Aufgabensammlung")
	private List<Aufgabensammlungselement> elemente;

	public static AufgabensammlungDetails createFromDB(final PersistenteAufgabensammlung aufgabensammlungDB) {

		AufgabensammlungDetails result = new AufgabensammlungDetails();
		result.geaendertDurch = aufgabensammlungDB.getGeaendertDurch();
		result.id = aufgabensammlungDB.getUuid();
		result.kommentar = aufgabensammlungDB.getKommentar();
		result.name = aufgabensammlungDB.getName();
		result.referenz = aufgabensammlungDB.getReferenz();
		result.referenztyp = aufgabensammlungDB.getReferenztyp();
		result.schwierigkeitsgrad = aufgabensammlungDB.getSchwierigkeitsgrad();
		result.freigegeben = aufgabensammlungDB.isFreigegeben();
		result.privat = aufgabensammlungDB.isPrivat();
		return result;
	}

	public void addElement(final Aufgabensammlungselement element) {

		if (this.elemente == null) {
			this.elemente = new ArrayList<>();		}

		this.elemente.add(element);
	}
}
