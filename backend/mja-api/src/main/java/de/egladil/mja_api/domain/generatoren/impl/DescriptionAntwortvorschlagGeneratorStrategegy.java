// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.Raetsel;

/**
 * DescriptionAntwortvorschlagGeneratorStrategegy
 */
public class DescriptionAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Raetsel raetsel) {

		Antwortvorschlag[] antwortVorschlaege = raetsel.getAntwortvorschlaege();

		if (antwortVorschlaege == null || antwortVorschlaege.length == 0) {

			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("\n");
		sb.append("\\begin{description}");
		sb.append("\n");

		for (Antwortvorschlag antwortvorschlag : antwortVorschlaege) {

			sb.append("\\item[(");
			sb.append(antwortvorschlag.getBuchstabe());
			sb.append(")] ");
			sb.append(antwortvorschlag.getText());

			sb.append("\n");
		}

		sb.append("\\end{description}\n");

		return sb.toString();
	}

}
