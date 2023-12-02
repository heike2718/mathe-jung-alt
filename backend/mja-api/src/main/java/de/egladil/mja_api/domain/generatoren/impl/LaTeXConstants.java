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

	String VALUE_PAR = "\\par\n";

	String VALUE_LINEBREAK = "\\\\\n";

	String HEADER_AUFGABE_NUMMER_SCHLUESSEL_PUNKTE = "{\\bf \\color{{color}} Aufgabe {nummer} - {schluessel} ({punkte}) }\\\\\n";

	String HEADER_AUFGABE_SCHLUESSEL_PUNKTE = "{\\bf \\color{{color}} Aufgabe {schluessel} ({punkte}) }\\\\\n";

	String HEADER_AUFGABE_NUMMER = "{\\bf Aufgabe {nummer} }\\\\\n";

	String HEADER_LOESUNG_NUMMER_SCHLUESSEL = "{\\bf Lösung {nummer} - {schluessel} }\\\\\n";

	String HEADER_LOESUNG_SCHLUESSEL = "{\\bf Lösung {schluessel}}\\\\\n";

	String HEADER_LOESUNG_NUMMER = "{\\bf Lösung {nummer} }\\\\\n";

	String ABSTAND_ITEMS = "\\par \n\\vspace{1ex}\n";

	String INCLUDEGRAPHICS_START = "\\begin{center} \\includegraphics[width=0.5\\linewidth]{.";

	String INCLUDEGRAPHICS_END = "} \\end{center}";

	String INPUT_AUFGABE = "{\\bf Aufgabe {nummer} }\\\\ \\input{./{schluessel}}\\par\n";

	String INPUT_LOESUNG = "{\\bf Lösung {nummer} }\\\\ \\input{./{schluessel}_l}\\par\n";

}
