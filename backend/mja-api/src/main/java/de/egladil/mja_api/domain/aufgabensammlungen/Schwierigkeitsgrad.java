// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Schwierigkeitsgrad
 */
@Schema(
	name = "Schwierigkeitsgrad",
	description = "enum Schwierigkeitsgrad für Aufgabensammlungen oder Quiz(e)")
public enum Schwierigkeitsgrad {

	ALLE("von Vorschule bis Erwachsene"),
	AB_NEUN("ab Klasse 9"),
	DREI_VIER("Klassen 3/4"),
	EINS("Klasse 1") {

		@Override
		public boolean isValidForMinikaenguruResources() {

			return true;
		}

	},
	EINS_ZWEI("Klassen 1/2"),
	FUENF_SECHS("Klassen 5/6"),
	GRUNDSCHULE("Grundschule"),
	IKID("Inklusion") {

		@Override
		public boolean isValidForMinikaenguruResources() {

			return true;
		}

	},
	SEK_1("Sekundarstufe 1"),
	SEK_2("Sekundarstufe 2"),
	SIEBEN_ACHT("Klassen 7/8"),
	VORSCHULE("Vorschule"),
	ZWEI("Klasse 2") {

		@Override
		public boolean isValidForMinikaenguruResources() {

			return true;
		}

	};

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

	public boolean isValidForMinikaenguruResources() {

		return false;
	}

}
