// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.upload;

/**
 * DateiTyp
 */
public enum DateiTyp {

	EPS("application/postscript", ".eps") {
		@Override
		public FileType getFileType() {

			return FileType.EPS;
		}

	};

	private final String tikaName;

	private final String suffixWithPoint;

	private DateiTyp(final String tikaName, final String suffixWithPoint) {

		this.tikaName = tikaName;
		this.suffixWithPoint = suffixWithPoint;
	}

	public String getTikaName() {

		return tikaName;
	}

	public static DateiTyp valueOfTikaName(final String tikaName) {

		for (DateiTyp dateiTyp : DateiTyp.values()) {

			if (dateiTyp.tikaName.equalsIgnoreCase(tikaName)) {

				return dateiTyp;
			}
		}

		return null;

	}

	public String getSuffixWithPoint() {

		return suffixWithPoint;
	}

	public abstract FileType getFileType();

}
