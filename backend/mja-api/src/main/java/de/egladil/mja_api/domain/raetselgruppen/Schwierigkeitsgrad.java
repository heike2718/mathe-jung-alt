// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

/**
 * Schwierigkeitsgrad
 */
public enum Schwierigkeitsgrad {

	AB_NEUN("ab Klasse 9"),
	DREI_VIER("Klassen 3/4"),
	EINS("Klasse 1"),
	EINS_ZWEI("Klassen 1/2"),
	FUENF_SECHS("Klassen 5/6"),
	GRUNDSCHULE("Grundschule"),
	IKID("Inklusion"),
	SEK_1("Sekundarstufe 1"),
	SEK_2("Sekundarstufe 2"),
	SIEBEN_ACHT("Klassen 7/8"),
	VORSCHULE("Vorschule"),
	ZWEI("Klasse 2");

	private final String label;

	/**
	 * @param label
	 */
	private Schwierigkeitsgrad(final String label) {

		this.label = label;
	}

	public String getLabel() {

		return label;
	}

}
