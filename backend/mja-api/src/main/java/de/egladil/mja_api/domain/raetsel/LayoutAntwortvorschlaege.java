// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * LayoutAntwortvorschlaege ANZEIGE_ANTWORTVORSCHLAEGE_TYP
 */
@Schema(
	name = "LayoutAntwortvorschlaege",
	description = "enum für das gewünschte Layout der Antwortvorschläge im Aufgabenimage. NOOP: keine Antwortvorschläge, ANKREUZTABELLE: zweizeilige Tabelle wie im Minikänguruwettbewerb - oben die Lösungsvorschläge - unten leer zum Ankreuzen, BUCHSTABEN: zweizeilige Tabelle - oben Lösungsbuchstaben, unten die Lösungsvorschläge, DESCRIPTION: zweispaltige Liste - links Lösungsbuchstaben, rechts die Lösungsvorschläge")
public enum LayoutAntwortvorschlaege {

	NOOP, // keine generierte, da in Quizaufgabe enthalten oder nicht multiple choice
	ANKREUZTABELLE, // zweizeilige Tabelle, Zeile 1 Antwortvorschläge, Zeile 2 leer
	BUCHSTABEN, // zweizeilige Tabelle, Zeile 1 Buchstaben, Zeile 2 Antwortvorschäge
	DESCRIPTION; // wie BUCHSTABEN, aber in \begin{description}...\end{description}

}
