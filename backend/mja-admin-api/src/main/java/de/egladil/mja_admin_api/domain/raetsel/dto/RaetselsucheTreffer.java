// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RaetselsucheTreffer enthält die Anzahl aller Treffer und eine Page der Treffermenge.
 */
public class RaetselsucheTreffer {

	@JsonProperty
	private long trefferGesamt = 0;

	@JsonProperty
	private List<RaetselsucheTrefferItem> treffer = new ArrayList<>();

	public long getTrefferGesamt() {

		return trefferGesamt;
	}

	public void setTrefferGesamt(final long trefferGesamt) {

		this.trefferGesamt = trefferGesamt;
	}

	public List<RaetselsucheTrefferItem> getTreffer() {

		return treffer;
	}

	public void setTreffer(final List<RaetselsucheTrefferItem> treffer) {

		this.treffer = treffer;
	}

}
