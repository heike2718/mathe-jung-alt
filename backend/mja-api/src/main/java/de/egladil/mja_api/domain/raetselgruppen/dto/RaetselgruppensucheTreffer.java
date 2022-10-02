// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RaetselgruppensucheTreffer
 */
@Schema(name = "RaetselgruppensucheTreffer", description = "Teilmenge der Treffer einer Suchanfrage")
public class RaetselgruppensucheTreffer {

	@JsonProperty
	@Schema(description = "Gesamtzahl aller Treffer der Suchanfrage")
	private long trefferGesamt = 0;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = RaetselgruppensucheTrefferItem.class,
		description = "die abgefragte Teilmenge aller Treffer der Suchanfrage (Page)")
	private List<RaetselgruppensucheTrefferItem> items = new ArrayList<>();

	public void setTrefferGesamt(final long trefferGesamt) {

		this.trefferGesamt = trefferGesamt;
	}

	public void addItem(final RaetselgruppensucheTrefferItem item) {

		items.add(item);
	}

	public long getTrefferGesamt() {

		return trefferGesamt;
	}

	public List<RaetselgruppensucheTrefferItem> getItems() {

		return items;
	}

}
