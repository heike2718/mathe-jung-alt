// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren;

/**
 * Verwendungszweck. Er bestimmt die Gruppierung der QuizItems im PDF oder LaTeX.
 */
public enum Verwendungszweck {

	ARBEITSBLATT("arbeitsblatt_"),
	KARTEI("kartei_"),
	LATEX(""),
	VORSCHAU("vorschau_") {

		@Override
		public boolean isHeadersWithNummerUndSchluessel() {

			return true;
		}
	};

	private final String filenamePrefix;

	/**
	 * @param filenamePrefix
	 */
	private Verwendungszweck(final String filenamePrefix) {

		this.filenamePrefix = filenamePrefix;
	}

	/**
	 * @return boolean default ist false
	 */
	public boolean isHeadersWithNummerUndSchluessel() {

		return false;
	}

	public String getFilenamePrefix() {

		return filenamePrefix;
	}
}
