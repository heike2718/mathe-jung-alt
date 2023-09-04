// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.util.List;

import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;

/**
 * RaetselgruppeGeneratorService
 */
public interface RaetselgruppeGeneratorService {

	/**
	 * Generiert LaTeX für die gegebene raetselguppe.
	 *
	 * @param  raetselgruppe
	 *                                  PersistenteRaetselgruppe Berechtigungsprüfung nimmr aufrufender Service vor.
	 * @param  aufgaben
	 *                                  List nur die Aufgaben, die gedruckt werden sollen. Vorauswahl trifft aufrufender Service.
	 * @param  verwendungszweck
	 *                                  Verwendungszweck entscheidet über die Gruppierung der Aufgaben.
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege wenn NOOP, werden keine Antwortvorschläge gedruckt. So können aus
	 *                                  multiple
	 *                                  choice- Aufgaben auch Arbeitsblätter werden.
	 * @return                          GeneratedFile - ein PDF oder eine LaTeX-Textdatei
	 */
	GeneratedFile generate(PersistenteRaetselgruppe raetselgruppe, List<Quizaufgabe> aufgaben, Verwendungszweck verwendungszweck, FontName font, Schriftgroesse schriftgroesse, LayoutAntwortvorschlaege layoutAntwortvorschlaege);
}
