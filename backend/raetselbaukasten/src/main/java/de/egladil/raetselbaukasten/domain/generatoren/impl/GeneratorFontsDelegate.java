// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import de.egladil.raetselbaukasten.domain.generatoren.FontName;

/**
 * GeneratorFontsDelegate
 */
public class GeneratorFontsDelegate {

	public String getTextLizenzFont(final FontName font) {

		switch (font) {

		case DRUCK_BY_WOK:

			return LaTeXTemplatesService.getInstance().getLizenzFontsDruckschrift();

		case FIBEL_NORD:
		case FIBEL_SUED:
			return LaTeXTemplatesService.getInstance().getLizenzFontsFibel();

		case STANDARD:
			return "";

		default:
			throw new IllegalArgumentException("Unexpected value: " + font);
		}
	}

}
