// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetsel.dto.GrafikInfo;
import de.egladil.mja_api.domain.semantik.AggregateRoot;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;

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
	private DomainEntityStatus status;

	@JsonProperty
	private String quelleId;

	@JsonProperty
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	private List<Deskriptor> deskriptoren;

	@JsonProperty
	private List<GrafikInfo> grafikInfos = new ArrayList<>();

	@JsonProperty
	private byte[] imageFrage;

	@JsonProperty
	private byte[] imageLoesung;

	@JsonProperty
	private byte[] raetselPDF;

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

		return AntwortvorschlaegeMapper.antwortvorschlaegeAsJSON(antwortvorschlaege);
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

	public byte[] getImageFrage() {

		return imageFrage;
	}

	public void setImageFrage(final byte[] imageFrage) {

		this.imageFrage = imageFrage;
	}

	public byte[] getImageLoesung() {

		return imageLoesung;
	}

	public void setImageLoesung(final byte[] imageLoesung) {

		this.imageLoesung = imageLoesung;
	}

	public DomainEntityStatus getStatus() {

		return status;
	}

	public Raetsel withStatus(final DomainEntityStatus status) {

		this.status = status;
		return this;
	}

	public List<GrafikInfo> getGrafikInfos() {

		return grafikInfos;
	}

	public void setGrafikInfos(final List<GrafikInfo> grafikInfos) {

		this.grafikInfos = grafikInfos;
	}
}
