// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

/**
 * LaTeXPlaceholder
 */
public enum LaTeXPlaceholder {

	ANTWORTVORSCHLAEGE("{antwortvorschlaege}"),
	ARRAYSTRETCH("{arraystretch}"),
	BUCHSTABE("{buchstabe}"),
	COLOR("{color}"),
	CONTENT("{content}"),
	CONTENT_FRAGE("{content-frage}"),
	CONTENT_LOESUNG("{content-loesung}"),
	FONT_NAME("{font}"),
	HEADER_FRAGE("{header-frage}"),
	HEADER_LOESUNG("{header-loesung}"),
	LIZENZ_FONTS("{lizenz-fonts}"),
	NUMMER("{nummer}"),
	CONTENT_RAETSEL_FRAGE_LOESUNG("{content-raetsel-frage-loesung}"),
	LOESUNGSBUCHSTABE("{loesungsbuchstabe}"),
	NEWPAGE("{newpage}"),
	PAR("{par}"),
	PUNKTE("{punkte}"),
	SCHLUESSEL("{schluessel}"),
	SCHRIFTGROESSE("{schriftgroesse}"),
	UEBERSCHRIFT("{ueberschrift}"),
	UEBERSCHRIFT_AUFGABEN("{ueberschrift-aufgaben}"),
	UEBERSCHRIFT_LOESUNGEN("{ueberschrift-loesungen}"),
	TRENNER_FRAGE_LOESUNG("{trenner-frage-loesung}");

	private final String placeholder;

	/**
	 * @param placeholder
	 */
	private LaTeXPlaceholder(final String placeholder) {

		this.placeholder = placeholder;
	}

	public String placeholder() {

		return placeholder;
	}

}
