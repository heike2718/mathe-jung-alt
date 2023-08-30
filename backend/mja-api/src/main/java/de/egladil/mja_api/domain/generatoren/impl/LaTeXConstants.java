// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

/**
 * LaTeXConstants
 */
public interface LaTeXConstants {

	String VALUE_NEWPAGE = "\\newpage\n";

	String VALUE_PAR = "\\par";

	String VALUE_LINEBREAK = "\\\\";

	String PATTERN_VALUE_LOESUNGSBUCHSTABE = "{\\it Lösungsbuchstabe ist {buchstabe}}\\par";

	String HEADER_AUFGABE_NUMMER_SCHLUESSEL_PUNKTE = "{\\bf \\color{{color}} Aufgabe {nummer} - {schluessel} ({punkte}) }\\\\";

	String HEADER_AUFGABE_SCHLUESSEL_PUNKTE = "{\\bf \\color{{color}} Aufgabe {schluessel} ({punkte}) }\\\\";

	String HEADER_AUFGABE_NUMMER = "{\\bf Aufgabe {nummer} }\\\\";

	String HEADER_LOESUNG_NUMMER_SCHLUESSEL = "{\\bf Lösung {nummer} - {schluessel} }\\\\";

	String HEADER_LOESUNG_SCHLUESSEL = "{\\bf Lösung {schluessel}}\\\\";

	String HEADER_LOESUNG_NUMMER = "{\\bf Lösung {nummer} }\\\\";

	String ABSTAND_ITEMS = "\\par \n\\vspace{1ex}\n";

}
