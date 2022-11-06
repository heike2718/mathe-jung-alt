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
	 * Generiert das PDF einer freigegebenen Rätselgruppe. Dabei werden die Lösungen mit einem newspace ans Ende gehängt, so dass
	 * man es wie Arbeitsblätter verwenden kann.
	 *
	 * @param  raetselgruppe
	 *                                  PersistenteRaetselgruppe
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	GeneratedFile generatePDFQuiz(PersistenteRaetselgruppe raetselgruppe, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Generiert de Vorschau einer Rätselgruppe, unabhängig davon, ob sie bereits freigegeben ist. Aufgaben und Lösungen werden
	 * zusammen gedruckt.
	 *
	 * @param  raetselgruppe
	 *                                  PersistenteRaetselgruppe
	 * @param  aufgaben
	 *                                  die Aufgaben zu diesem Quiz. Diese sind bereits sortiert.
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	GeneratedFile generateVorschauPDFQuiz(PersistenteRaetselgruppe raetselgruppe, List<Quizaufgabe> aufgaben, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

}
