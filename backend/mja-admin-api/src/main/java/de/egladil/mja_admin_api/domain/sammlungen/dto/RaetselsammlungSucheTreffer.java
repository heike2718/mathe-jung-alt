// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RaetselsammlungSucheTreffer
 */
public class RaetselsammlungSucheTreffer {

	@JsonProperty
	private long trefferGesamt = 0;

	@JsonProperty
	private List<RaetselsammlungSucheTrefferItem> treffer = new ArrayList<>();

}
