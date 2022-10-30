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
	CONTENT("{content}"),
	CONTENT_FRAGE("{content-frage}"),
	CONTENT_LOESUNG("{content-loesung}"),
	CONTENT_RAETSEL_FRAGE_LOESUNG("{content-raetsel-frage-loesung}"),
	LOESUNGSBUCHSTABE("{loesungsbuchstabe}"),
	NEWPAGE("{newpage}"),
	PAR("{par}");

	private final String placeholder;

	/**
	 * @param placeholder
	 */
	private LaTeXPlaceholder(final String placeholder) {

		this.placeholder = placeholder;
	}

	public String getPlaceholder() {

		return placeholder;
	}

}
