// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;

/**
 * DescriptionAntwortvorschlagGeneratorStrategegy
 */
public class DescriptionAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		if (antwortvorschlaege == null || antwortvorschlaege.length == 0) {

			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("\n");
		sb.append("\\begin{description}");
		sb.append("\n");

		for (Antwortvorschlag antwortvorschlag : antwortvorschlaege) {

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
