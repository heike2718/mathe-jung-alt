// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mathe_jung_alt_ws.domain.AbstractDomainEntity;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;

/**
 * RaetselOpenDataDto ist ein Dto, welches Anwendungen zum Bauen von Quizes verwenden können. Initialer Zweck ist die Darstellung
 * der Statistik der Minikänguru-Wettbewerbe.<br>
 * <br>
 * Suche für Minikänguru erfolgt anhand der Deskriptoren
 * <ul>
 * <li>Minikänguru</li>
 * <li>4stelliges Wettbewerbsjahr</li>
 * <li>Stufe (IKID,EINS,ZWEI</li>
 * <li>Aufgabennummer (A-1, ..., C-5)</li>
 * </ul>
 * Admin muss dafür sorgen, dass jedes Raetsel, das für einen Minikänguru-Wettbewerb verwendet wird, anhand aller genannten
 * Deskriptoren eindeutig identifiziert werden kann.<br>
 * <br>
 * Die Open-Source-Suche mit "Minikänguru" darf vor dem 1.8. nur Treffer aus den Vorjahren liefern. Ebenso sind Treffer, die nicht
 * den Status FREIGEGEBEN haben, auszuschließen.
 */
public class RaetselOpenDataDto extends AbstractDomainEntity {

	@JsonProperty
	private String schluessel;

	@JsonProperty
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	private byte[] imageFrage;

	@JsonProperty
	private byte[] imageLoesung;

	public String getSchluessel() {

		return schluessel;
	}

	public RaetselOpenDataDto withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public RaetselOpenDataDto withAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
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

}
