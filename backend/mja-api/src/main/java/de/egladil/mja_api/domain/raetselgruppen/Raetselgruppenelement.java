// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;

/**
 * Raetselgruppenelement
 */
@Schema(name = "Raetselgruppenelement", description = "Element einer Rätselgruppe")
public class Raetselgruppenelement {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neues Element")
	private String id;

	@JsonProperty
	@Schema(description = "Nummer, die die Reihenfolge innerhalb der Gruppe festlegt")
	private String nummer;

	@JsonProperty
	@Schema(description = "Punkte für dieses Rätsel, multipliziert mit 100, um Rundungsfehler zu vermeiden.")
	private int punkte;

	@JsonProperty
	@Schema(description = "fachlicher SCHLUESSEL des Rätsels, nicht Teil der persistenten Daten. Nützlich für die Anzeige")
	private String raetselSchluessel;

	@JsonProperty
	@Schema(description = "Name des Rätsels, nicht Teil der Daten des Elements. Nützlich für die Anzeige")
	private String name;

	/**
	 * Erzeugt ein Objekt aus den verschiedenen Daten.
	 *
	 * @param  aufgabe
	 *                 PersistenteAufgabeReadonly
	 * @param  element
	 *                 PersistentesRaetselgruppenelement
	 * @return         Raetselgruppenelement
	 */
	public static Raetselgruppenelement merge(final PersistenteAufgabeReadonly aufgabe, final PersistentesRaetselgruppenelement element) {

		Raetselgruppenelement result = new Raetselgruppenelement();
		result.id = element.uuid;
		result.nummer = element.nummer;
		result.punkte = element.punkte;
		result.raetselSchluessel = aufgabe.schluessel;
		result.name = aufgabe.name;
		return result;
	}

	public String getNummer() {

		return nummer;
	}

	public String getId() {

		return id;
	}

	public int getPunkte() {

		return punkte;
	}

	public String getRaetselSchluessel() {

		return raetselSchluessel;
	}

	public String getName() {

		return name;
	}
}
