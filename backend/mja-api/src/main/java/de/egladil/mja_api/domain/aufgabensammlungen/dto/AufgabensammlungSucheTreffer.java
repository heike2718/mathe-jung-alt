// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AufgabensammlungSucheTreffer
 */
@Schema(name = "AufgabensammlungSucheTreffer", description = "Teilmenge der Treffer einer Suchanfrage")
public class AufgabensammlungSucheTreffer {

	@JsonProperty
	@Schema(description = "Gesamtzahl aller Treffer der Suchanfrage")
	private long trefferGesamt = 0;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = AufgabensammlungSucheTrefferItem.class,
		description = "die abgefragte Teilmenge aller Treffer der Suchanfrage (Page)")
	private List<AufgabensammlungSucheTrefferItem> items = new ArrayList<>();

	public void setTrefferGesamt(final long trefferGesamt) {

		this.trefferGesamt = trefferGesamt;
	}

	public void addItem(final AufgabensammlungSucheTrefferItem item) {

		items.add(item);
	}

	public long getTrefferGesamt() {

		return trefferGesamt;
	}

	public List<AufgabensammlungSucheTrefferItem> getItems() {

		return items;
	}

}
