// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

/**
 * AnzeigeAntwortvorschlaegeTyp
 */
public enum AnzeigeAntwortvorschlaegeTyp {

	NOOP, // keine generierte, da in Aufgabe enthalten
	ANKREUZTABELLE, // zweizeilige Tabelle, Zeile 1 Antwortvorschläge, Zeile 2 leer
	BUCHSTABEN, // zweizeilige Tabelle, Zeile 1 Buchstaben, Zeile 2 Antwortvorschäge
	DESCRIPTION; // description-Umgebung

}
