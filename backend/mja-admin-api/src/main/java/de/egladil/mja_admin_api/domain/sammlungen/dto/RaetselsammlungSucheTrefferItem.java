// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_admin_api.domain.DomainEntityStatus;
import de.egladil.mja_admin_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_admin_api.domain.sammlungen.Schwierigkeitsgrad;

/**
 * RaetselsammlungSucheTrefferItem
 */
public class RaetselsammlungSucheTrefferItem {

	@JsonProperty
	private String id;

	@JsonProperty
	private String name;

	@JsonProperty
	private String kommentar;

	@JsonProperty
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@JsonProperty
	private Referenztyp referenztyp;

	@JsonProperty
	private String referenz;

	@JsonProperty
	private DomainEntityStatus status;

	@JsonProperty
	private int anzahlElemente;

}
