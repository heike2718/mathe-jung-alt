// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;

/**
 * RaetselsucheTrefferItem sind die Infos zu einem Rätsel.
 */
public class RaetselsucheTrefferItem {

	@JsonProperty
	private String id;

	@JsonProperty
	private String schluessel;

	@JsonProperty
	private String name;

	@JsonProperty
	private String kommentar;

	@JsonProperty
	private DomainEntityStatus status;

	@JsonProperty
	private List<Deskriptor> deskriptoren;

	public String getId() {

		return id;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public String getName() {

		return name;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public RaetselsucheTrefferItem withId(final String id) {

		this.id = id;
		return this;
	}

	public RaetselsucheTrefferItem withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public RaetselsucheTrefferItem withName(final String name) {

		this.name = name;
		return this;
	}

	public RaetselsucheTrefferItem withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

	public String deskriptorenIds() {

		List<String> ids = deskriptoren.stream().map(d -> d.id + "").toList();
		return StringUtils.join(ids, ',');
	}

	public String getKommentar() {

		return kommentar;
	}

	public RaetselsucheTrefferItem withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

	public DomainEntityStatus getStatus() {

		return status;
	}

	public RaetselsucheTrefferItem withStatus(final DomainEntityStatus status) {

		this.status = status;
		return this;
	}

}
