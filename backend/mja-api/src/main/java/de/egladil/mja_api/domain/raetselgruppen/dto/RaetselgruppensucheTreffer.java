// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RaetselgruppensucheTreffer
 */
public class RaetselgruppensucheTreffer {

	@JsonProperty
	private long trefferGesamt = 0;

	@JsonProperty
	private List<RaetselgruppensucheTrefferItem> items = new ArrayList<>();

	public void setTrefferGesamt(final long trefferGesamt) {

		this.trefferGesamt = trefferGesamt;
	}

	public void addItem(final RaetselgruppensucheTrefferItem item) {

		items.add(item);
	}

}
