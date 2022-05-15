// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import java.util.List;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * RaetselPayloadDaten
 */
public class RaetselPayloadDaten {

	private String id;

	private String schluessel;

	private String titel;

	private String kommentar;

	private String frage;

	private String loesung;

	private String quelleId;

	private List<Antwortvorschlag> antwortvorschlaege;

	private List<Deskriptor> deskriptoren;

	public String getId() {

		return id;
	}

	public void setId(final String id) {

		this.id = id;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public void setSchluessel(final String schluessel) {

		this.schluessel = schluessel;
	}

	public String getTitel() {

		return titel;
	}

	public void setTitel(final String titel) {

		this.titel = titel;
	}

	public String getKommentar() {

		return kommentar;
	}

	public void setKommentar(final String kommentar) {

		this.kommentar = kommentar;
	}

	public String getFrage() {

		return frage;
	}

	public void setFrage(final String frage) {

		this.frage = frage;
	}

	public String getLoesung() {

		return loesung;
	}

	public void setLoesung(final String loesung) {

		this.loesung = loesung;
	}

	public String getQuelleId() {

		return quelleId;
	}

	public void setQuelleId(final String quelleId) {

		this.quelleId = quelleId;
	}

	public List<Antwortvorschlag> getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public void setAntwortvorschlaege(final List<Antwortvorschlag> antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public void setDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
	}

}
