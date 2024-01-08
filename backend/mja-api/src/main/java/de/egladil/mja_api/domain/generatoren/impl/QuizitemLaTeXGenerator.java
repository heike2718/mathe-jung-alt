// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.TrennerartFrageLoesung;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * QuizitemLaTeXGenerator
 */
@ApplicationScoped
public class QuizitemLaTeXGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuizitemLaTeXGenerator.class);

	/**
	 * Generiert ein Stück LaTeX, das in ein Dokument eingefügt werden kann. Es werden Frage und Lösung hintereinander eingefügt,
	 * entweder mit kleinem Abstand oder mit Seitenumbruch.
	 *
	 * @param  input
	 *               RaetselGeneratorinput die Texte des Rätsels, die gedruckt werden sollen.
	 * @return       String
	 */
	public String generateLaTeXFrageLoesung(final RaetselGeneratorinput input, final TrennerartFrageLoesung trennerFrageLoesung, final boolean printAsMultipleChoice) {

		String template = new String(LaTeXTemplatesService.getInstance().getTemplatePDFRaetselFrageLoesung());

		template = template.replace(LaTeXPlaceholder.COLOR.placeholder(), input.getTextColor());
		template = template.replace(LaTeXPlaceholder.NUMMER.placeholder(), input.getNummer());

		template = template.replace(LaTeXPlaceholder.HEADER_FRAGE.placeholder(), getHeaderFrage(input));
		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), input.getFrage());

		boolean isMultipleChoice = input.getAntwortvorschlaege() != null && input.getAntwortvorschlaege().length > 0;

		if (isGenerateAntwortvorschlaegeToLaTeX(input, printAsMultipleChoice, isMultipleChoice)) {

			String antworten = AntwortvorschlagGeneratorStrategegy.create(input.getLayoutAntwortvorschlaege())
				.generateLaTeXAntwortvorschlaege(input.getAntwortvorschlaege());

			template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), antworten);

		} else {

			template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), "");
		}

		if (StringUtils.isNotBlank(input.getLoesung())) {

			template = template.replace(LaTeXPlaceholder.TRENNER_FRAGE_LOESUNG.placeholder(), trennerFrageLoesung.getLeTeX());
			template = template.replace(LaTeXPlaceholder.HEADER_LOESUNG.placeholder(), getHeaderLoesung(input));

			if (isMultipleChoice && printAsMultipleChoice) {

				String loesungsbuchstabe = getTextLoesungsbuchstabe(input.getAntwortvorschlaege());

				if (StringUtils.isNotBlank(loesungsbuchstabe)) {

					template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), loesungsbuchstabe);
				} else {

					template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), "");
				}

			} else {

				template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), "");
			}

			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), input.getLoesung());

		} else {

			template = template.replace(LaTeXPlaceholder.TRENNER_FRAGE_LOESUNG.placeholder(), "");
			template = template.replace(LaTeXPlaceholder.HEADER_LOESUNG.placeholder(), "");
			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), "");
			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), "");
		}

		return template;
	}

	/**
	 * Generiert ein Stück LaTeX, das in ein Dokument eingefügt werden kann. Es wird nur die Frage eingefügt.
	 *
	 * @param  input
	 *               RaetselGeneratorinput die Texte des Rätsels, die gedruckt werden sollen.
	 * @return       String
	 */
	public String generateLaTeXFrage(final RaetselGeneratorinput input, final boolean printAsMultipleChoice) {

		String template = new String(LaTeXTemplatesService.getInstance().getTemplatePDFRaetselFrage());

		template = template.replace(LaTeXPlaceholder.HEADER_FRAGE.placeholder(), getHeaderFrage(input));
		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), input.getFrage());

		boolean isMultipleChoice = input.getAntwortvorschlaege() != null && input.getAntwortvorschlaege().length > 0;

		if (isGenerateAntwortvorschlaegeToLaTeX(input, printAsMultipleChoice, isMultipleChoice)) {

			String antworten = AntwortvorschlagGeneratorStrategegy.create(input.getLayoutAntwortvorschlaege())
				.generateLaTeXAntwortvorschlaege(input.getAntwortvorschlaege());

			template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), antworten);

		} else {

			template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), "");
		}

		return template;
	}

	/**
	 * Wenn es überhaupt Antwortvorschläge gibt, diese nicht eingebettet sind und der Druckauftrag Antwortvorschläge verlangt, dann
	 * true, anderenfalls false.
	 *
	 * @param  input
	 * @param  printAsMultipleChoice
	 * @param  isMultipleChoice
	 * @return                       boolean
	 */
	boolean isGenerateAntwortvorschlaegeToLaTeX(final RaetselGeneratorinput input, final boolean printAsMultipleChoice, final boolean isMultipleChoice) {

		return isMultipleChoice && printAsMultipleChoice && !input.isAntwortvorschlaegeEingebettet();
	}

	/**
	 * Generiert ein Stück LaTeX, das in ein Dokument eingefügt werden kann. Es wird nur die Frage eingefügt.
	 *
	 * @param  input
	 *               RaetselGeneratorinput die Texte des Rätsels, die gedruckt werden sollen.
	 * @return       String
	 */
	public String generateLaTeXLoesung(final RaetselGeneratorinput input, final boolean printAsMultipleChoice) {

		if (StringUtils.isBlank(input.getLoesung())) {

			LOGGER.warn("raetsel {} hat keine Loesung!", input.getSchluessel());

			return "";
		}

		String template = new String(LaTeXTemplatesService.getInstance().getTemplatePDFRaetselLoesung());

		template = template.replace(LaTeXPlaceholder.HEADER_LOESUNG.placeholder(), getHeaderLoesung(input));

		boolean isMultipleChoice = input.getAntwortvorschlaege() != null && input.getAntwortvorschlaege().length > 0;

		if (isMultipleChoice && printAsMultipleChoice) {

			String replacement = getTextLoesungsbuchstabe(input.getAntwortvorschlaege());

			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), replacement);

		} else {

			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), "");
		}

		template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), input.getLoesung());
		return template;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

	String getHeaderFrage(final RaetselGeneratorinput input) {

		String result = null;

		if (!input.getVerwendungszweck().isHeadersWithNummerUndSchluessel()) {

			result = LaTeXConstants.HEADER_AUFGABE_NUMMER;
			result = result.replace(LaTeXPlaceholder.NUMMER.placeholder(), input.getNummer());
			LOGGER.debug(result);

			return result;
		}

		if (input.getNummer().equals(input.getSchluessel())) {

			result = LaTeXConstants.HEADER_AUFGABE_SCHLUESSEL_PUNKTE;

		} else {

			result = LaTeXConstants.HEADER_AUFGABE_NUMMER_SCHLUESSEL_PUNKTE;
			result = result.replace(LaTeXPlaceholder.NUMMER.placeholder(), input.getNummer());
		}

		result = result.replace(LaTeXPlaceholder.COLOR.placeholder(), input.getTextColor());
		result = result.replace(LaTeXPlaceholder.PUNKTE.placeholder(), input.getPunkte() + "");
		result = result.replace(LaTeXPlaceholder.SCHLUESSEL.placeholder(), input.getSchluessel());

		LOGGER.debug(result);

		return result;
	}

	String getHeaderLoesung(final RaetselGeneratorinput input) {

		String result = null;

		if (!input.getVerwendungszweck().isHeadersWithNummerUndSchluessel()) {

			result = LaTeXConstants.HEADER_LOESUNG_NUMMER;
			result = result.replace(LaTeXPlaceholder.NUMMER.placeholder(), input.getNummer());
			LOGGER.debug(result);

			return result;
		}

		if (input.getNummer().equals(input.getSchluessel())) {

			result = LaTeXConstants.HEADER_LOESUNG_SCHLUESSEL;

		} else {

			result = LaTeXConstants.HEADER_LOESUNG_NUMMER_SCHLUESSEL;
			result = result.replace(LaTeXPlaceholder.NUMMER.placeholder(), input.getNummer());
		}

		result = result.replace(LaTeXPlaceholder.SCHLUESSEL.placeholder(), input.getSchluessel());

		LOGGER.debug(result);

		return result;
	}

}
