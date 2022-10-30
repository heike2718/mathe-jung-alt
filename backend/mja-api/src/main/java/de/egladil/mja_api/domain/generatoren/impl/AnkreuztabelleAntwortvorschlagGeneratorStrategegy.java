// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;

/**
 * AnkreuztabelleAntwortvorschlagGeneratorStrategegy
 */
public class AnkreuztabelleAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		if (antwortvorschlaege == null || antwortvorschlaege.length == 0) {

			return "";
		}

		String tableType = PATTERN_TABLETYPE.replace("#", "" + antwortvorschlaege.length);
		StringBuffer sb = new StringBuffer();

		sb.append("\\begin{center}\n");
		sb.append("\\begin{tabular}");
		sb.append(tableType);
		sb.append("\n\\hline\n");

		for (int i = 0; i < antwortvorschlaege.length; i++) {

			Antwortvorschlag antwortvorschlag = antwortvorschlaege[i];

			sb.append(antwortvorschlag.getText());

			if (i < antwortvorschlaege.length - 1) {

				sb.append(" & ");
			}
		}
		sb.append("\\\\\n\\hline\n");

		for (int i = 0; i < antwortvorschlaege.length; i++) {

			sb.append("");

			if (i < antwortvorschlaege.length - 1) {

				sb.append(" & ");
			}
		}

		sb.append("\\\\\n\\hline\n");
		sb.append("\\end{tabular}\n");
		sb.append("\\end{center}\n");

		return sb.toString();
	}

}
