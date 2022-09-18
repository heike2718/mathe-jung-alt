// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

/**
 * Outputformat
 */
public enum Outputformat {

	PDF {

		@Override
		public String getFilenameExtension() {

			return ".pdf";
		}

	},

	PNG {

		@Override
		public String getFilenameExtension() {

			return ".png";
		}

	};

	public abstract String getFilenameExtension();

}
