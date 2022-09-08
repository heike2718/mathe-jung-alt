// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

/**
 * LayoutAntwortvorschlaege ANZEIGE_ANTWORTVORSCHLAEGE_TYP
 */
public enum LayoutAntwortvorschlaege {

	NOOP, // keine generierte, da in Aufgabe enthalten oder nicht multiple choice
	ANKREUZTABELLE, // zweizeilige Tabelle, Zeile 1 Antwortvorschläge, Zeile 2 leer
	BUCHSTABEN, // zweizeilige Tabelle, Zeile 1 Buchstaben, Zeile 2 Antwortvorschäge
	DESCRIPTION; // wie BUCHSTABEN, aber in \begin{description}...\end{description}

}
