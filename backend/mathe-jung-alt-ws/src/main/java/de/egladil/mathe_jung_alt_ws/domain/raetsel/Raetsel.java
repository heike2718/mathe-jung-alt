// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.AbstractDomainEntity;
import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.semantik.AggregateRoot;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * Raetsel
 */
@AggregateRoot
public class Raetsel extends AbstractDomainEntity {

	@JsonProperty
	private String schluessel;

	@JsonProperty
	private String name;

	@JsonProperty
	private String frage;

	@JsonProperty
	private String loesung;

	@JsonProperty
	private String kommentar;

	@JsonProperty
	private String quelleId;

	@JsonProperty
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	private List<Deskriptor> deskriptoren;

	/**
	 *
	 */
	protected Raetsel() {

		super();

	}

	/**
	 * @param uuid
	 */
	public Raetsel(final String uuid) {

		super(uuid);

	}

	public String antwortvorschlaegeAsJSON() {

		if (antwortvorschlaege == null) {

			return null;
		}

		try {

			return new ObjectMapper().writeValueAsString(this.antwortvorschlaege);
		} catch (JsonProcessingException e) {

			throw new MjaRuntimeException("konnte antwortvorschlaege nicht serialisieren: " + e.getMessage(), e);

		}
	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof Raetsel)) {

			return false;
		}

		return super.equals(obj);
	}

	public String getSchluessel() {

		return schluessel;
	}

	public Raetsel withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public String getName() {

		return name;
	}

	public Raetsel withName(final String name) {

		this.name = name;
		return this;
	}

	public String getFrage() {

		return frage;
	}

	public Raetsel withFrage(final String frage) {

		this.frage = frage;
		return this;
	}

	public String getLoesung() {

		return loesung;
	}

	public Raetsel withLoesung(final String loesung) {

		this.loesung = loesung;
		return this;
	}

	public String getKommentar() {

		return kommentar;
	}

	public Raetsel withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

	public String getQuelleId() {

		return quelleId;
	}

	public Raetsel withQuelleId(final String quelleId) {

		this.quelleId = quelleId;
		return this;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public Raetsel withAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
		return this;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public Raetsel withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

}
