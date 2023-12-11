// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

/**
 * RaetselHerkunftTyp
 */
public enum RaetselHerkunftTyp {

	EIGENKREATION("selbst erstellt"),
	ZITAT("zitiert"),
	ADAPTATION("adaptiert");

	private final String beschreibung;

	RaetselHerkunftTyp(final String beschreibung) {

		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {

		return beschreibung;
	}

}
