// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.GeneratorUtils;

/**
 * AufgabenLoesungenLaTeXGeneratorDelegate
 */
public class AufgabenLoesungenLaTeXGeneratorDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabenLoesungenLaTeXGeneratorDelegate.class);

	String printContentAufgaben(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final AufgabensammlungGeneratorInput input) {

		final Verwendungszweck verwendungszweck = input.getVerwendungszweck();

		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				RaetselLaTeXDto raetsel = opt.get();

				RaetselGeneratorinput raetselInput = new RaetselGeneratorinput()
					.withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
					.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
					.withLayoutAntwortvorschlaege(input.getLayoutAntwortvorschlaege())
					.withNummer(aufgabe.getNummer())
					.withSchluessel(aufgabe.getSchluessel())
					.withVerwendungszweck(verwendungszweck)
					.withPunkten(aufgabe.getPunkte());

				if (count > 0) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}

				boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(input.getLayoutAntwortvorschlaege(),
					aufgabe);

				String text = quizitemLaTeXGenerator.generateLaTeXFrage(raetselInput, printAsMultipleChoice);
				sb.append(text);
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

	String printContentLoesungen(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final AufgabensammlungGeneratorInput input) {

		final Verwendungszweck verwendungszweck = input.getVerwendungszweck();
		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				RaetselLaTeXDto raetsel = opt.get();

				RaetselGeneratorinput raetselInput = new RaetselGeneratorinput()
					.withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
					.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
					.withLayoutAntwortvorschlaege(input.getLayoutAntwortvorschlaege())
					.withNummer(aufgabe.getNummer())
					.withSchluessel(aufgabe.getSchluessel())
					.withVerwendungszweck(verwendungszweck)
					.withPunkten(aufgabe.getPunkte());

				if (count > 0) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}

				boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(input.getLayoutAntwortvorschlaege(),
					aufgabe);

				String text = quizitemLaTeXGenerator.generateLaTeXLoesung(raetselInput, printAsMultipleChoice);
				sb.append(text);

				if (GeneratorUtils.shouldPrintTrenner(aufgaben.size(), count)) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

}
