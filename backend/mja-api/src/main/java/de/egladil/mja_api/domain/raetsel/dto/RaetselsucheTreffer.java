// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RaetselsucheTreffer enthält die Anzahl aller Treffer und eine Page der Treffermenge.
 */
@Schema(name = "RaetselsucheTreffer", description = "Teilmenge der Treffer einer Suchanfrage")
public class RaetselsucheTreffer {

	@JsonProperty
	@Schema(description = "Gesamtzahl aller Treffer der Suchanfrage")
	private long trefferGesamt = 0;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = RaetselsucheTrefferItem.class,
		description = "die abgefragte Teilmenge aller Treffer der Suchanfrage (Page)")
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
