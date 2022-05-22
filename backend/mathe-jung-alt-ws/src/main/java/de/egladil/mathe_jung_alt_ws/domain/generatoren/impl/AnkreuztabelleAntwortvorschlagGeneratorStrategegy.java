// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * AnkreuztabelleAntwortvorschlagGeneratorStrategegy
 */
public class AnkreuztabelleAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Raetsel raetsel) {

		Antwortvorschlag[] antwortVorschlaege = raetsel.getAntwortvorschlaege();

		if (antwortVorschlaege == null || antwortVorschlaege.length == 0) {

			return "";
		}

		String tableType = PATTERN_TABLETYPE.replace("#", "" + antwortVorschlaege.length);
		StringBuffer sb = new StringBuffer();

		sb.append("\\begin{center}\n");
		sb.append("\\begin{tabular}");
		sb.append(tableType);
		sb.append("\n\\hline\n");

		for (int i = 0; i < antwortVorschlaege.length; i++) {

			Antwortvorschlag antwortvorschlag = antwortVorschlaege[i];

			sb.append(antwortvorschlag.getText());

			if (i < antwortVorschlaege.length - 1) {

				sb.append(" & ");
			}
		}
		sb.append("\\\\\n\\hline\n");

		for (int i = 0; i < antwortVorschlaege.length; i++) {

			sb.append("");

			if (i < antwortVorschlaege.length - 1) {

				sb.append(" & ");
			}
		}

		sb.append("\\\\\n\\hline\n");
		sb.append("\\end{tabular}\n");
		sb.append("\\end{center}\n");

		return sb.toString();
	}

}
