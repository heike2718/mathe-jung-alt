// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.Raetsel;

/**
 * BuchstabenAntwortvorschlagGeneratorStrategegy
 */
public class BuchstabenAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

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

			sb.append(antwortvorschlag.getBuchstabe());

			if (i < antwortVorschlaege.length - 1) {

				sb.append(" & ");
			}
		}
		sb.append("\\\\\n\\hline\n");

		for (int i = 0; i < antwortVorschlaege.length; i++) {

			Antwortvorschlag antwortvorschlag = antwortVorschlaege[i];

			sb.append(antwortvorschlag.getText());

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
