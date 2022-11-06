// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;

/**
 * LoesungsbuchstabeTextGenerator
 */
public class LoesungsbuchstabeTextGenerator {

	/**
	 * Generiert den Text, mit dem das LaTeX einer Lösung beginnen soll.
	 *
	 * @param  antwortvorschlaege
	 * @return
	 */
	public String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		if (antwortvorschlaege == null || antwortvorschlaege.length == 0) {

			return "";
		}

		Optional<Antwortvorschlag> optKorrekter = Arrays.stream(antwortvorschlaege).filter(av -> av.isKorrekt()).findFirst();

		if (optKorrekter.isEmpty()) {

			return "";
		}

		Antwortvorschlag korrekter = optKorrekter.get();

		if (StringUtils.isNotBlank(korrekter.getText()) && !korrekter.getText().equals(korrekter.getBuchstabe())) {

			return "{\\bf Lösung ist " + korrekter.getBuchstabe() + " (" + korrekter.getText() + ")}\\\\ \\vspace{1ex}";
		}

		return "{\\bf Lösung ist " + korrekter.getBuchstabe() + "}\\\\ \\vspace{1ex}";
	}
}
