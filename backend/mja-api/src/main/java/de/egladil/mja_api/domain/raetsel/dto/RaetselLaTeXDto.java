// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselLaTeXDto die Daten, die zum Generieren erforderlich sind.
 */
public class RaetselLaTeXDto {

	private String id;

	private String schluessel;

	private String frage;

	private String loesung;

	public static RaetselLaTeXDto mapFromDB(final PersistentesRaetsel raetsel) {

		RaetselLaTeXDto result = new RaetselLaTeXDto();
		result.id = raetsel.uuid;
		result.schluessel = raetsel.schluessel;
		result.frage = raetsel.frage;
		result.loesung = raetsel.loesung;
		return result;
	}

	public String getId() {

		return id;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public String getFrage() {

		return frage;
	}

	public String getLoesung() {

		return loesung;
	}

}
