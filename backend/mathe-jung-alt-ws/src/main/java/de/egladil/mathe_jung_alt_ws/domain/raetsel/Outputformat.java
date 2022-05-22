// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

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
	PNG{

		@Override
		public String getFilenameExtension() {
			return ".png";
		}

	};

	public abstract String getFilenameExtension();

}
