// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.aufgabensammlungen.Aufgabensammlungselement;
import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.aufgabensammlungen.impl.AufgabensammlungselementComparator;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;

/**
 * AufgabensammlungDetails
 */
@Schema(
	name = "AufgabensammlungDetails",
	description = "Details einer Aufgabensammlung für die Anzeige oder zum Generieren von PDF oder LaTeX. Eine Rätselruppe ist eine freie Zusammenstellung von Rätseln. Ziel ist die Erzeugung eines PDFs. Die Rästel werden in der Reihenfolge gedruckt, in der sie in der Aufgabensammlung eingetragen werden.")
public class AufgabensammlungDetails {

	@JsonProperty
	@Schema(description = "technische ID")
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
	private List<Aufgabensammlungselement> elemente = new ArrayList<>();

	public static AufgabensammlungDetails createFromDB(final PersistenteAufgabensammlung aufgabensammlungDB) {

		AufgabensammlungDetails result = new AufgabensammlungDetails();
		result.geaendertDurch = aufgabensammlungDB.geaendertDurch;
		result.id = aufgabensammlungDB.uuid;
		result.kommentar = aufgabensammlungDB.kommentar;
		result.name = aufgabensammlungDB.name;
		result.referenz = aufgabensammlungDB.referenz;
		result.referenztyp = aufgabensammlungDB.referenztyp;
		result.schwierigkeitsgrad = aufgabensammlungDB.schwierigkeitsgrad;
		result.freigegeben = aufgabensammlungDB.freigegeben;
		result.privat = aufgabensammlungDB.privat;
		return result;
	}

	public void addElement(final Aufgabensammlungselement element) {

		this.elemente.add(element);
	}

	public void sortElemente() {

		Collections.sort(elemente, new AufgabensammlungselementComparator());
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

	public String getGeaendertDurch() {

		return geaendertDurch;
	}

	public List<Aufgabensammlungselement> getElemente() {

		return elemente;
	}

	public boolean isSchreibgeschuetzt() {

		return schreibgeschuetzt;
	}

	public void markiereAlsAenderbar() {

		this.schreibgeschuetzt = false;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public boolean isPrivat() {

		return privat;
	}
}
