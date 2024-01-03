// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.dto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;

/**
 * MediensucheResult
 */
@Schema(name = "MediensucheResult", description = "Resultat einer Mediensuche")
public class MediensucheResult {

	@JsonProperty
	@Schema(description = "Gesamtzahl aller Treffer der Suchanfrage")
	private long trefferGesamt = 0;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = RaetselsucheTrefferItem.class,
		description = "die abgefragte Teilmenge aller Treffer der Suchanfrage (Page)")
	private List<MediensucheTrefferItem> treffer = new ArrayList<>();

	public long getTrefferGesamt() {

		return trefferGesamt;
	}

	public void setTrefferGesamt(final long trefferGesamt) {

		this.trefferGesamt = trefferGesamt;
	}

	public List<MediensucheTrefferItem> getTreffer() {

		return treffer;
	}

	public void setTreffer(final List<MediensucheTrefferItem> treffer) {

		this.treffer = treffer;
	}

}
