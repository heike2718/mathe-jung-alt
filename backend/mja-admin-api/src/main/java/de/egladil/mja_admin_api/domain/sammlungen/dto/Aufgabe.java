// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_admin_api.domain.raetsel.Antwortvorschlag;

/**
 * Aufgabe
 */
public class Aufgabe {

	@JsonProperty
	private String nummer;

	@JsonProperty
	private String schluessel;

	@JsonProperty
	private String quelle;

	@JsonProperty
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	private byte[] imageFrage;

	@JsonProperty
	private byte[] imageLoesung;

	@Override
	public int hashCode() {

		return Objects.hash(nummer);
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aufgabe other = (Aufgabe) obj;
		return Objects.equals(nummer, other.nummer);
	}

	public String getNummer() {

		return nummer;
	}

	public void setNummer(final String nummer) {

		this.nummer = nummer;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public void setSchluessel(final String schluessel) {

		this.schluessel = schluessel;
	}

	public String getQuelle() {

		return quelle;
	}

	public void setQuelle(final String quelle) {

		this.quelle = quelle;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public void setAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
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

}
