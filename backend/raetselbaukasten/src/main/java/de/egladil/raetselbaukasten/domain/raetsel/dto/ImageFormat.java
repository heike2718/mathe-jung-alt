// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel.dto;

/**
 * ImageFormat
 */
public enum ImageFormat {

	PNG {

		@Override
		public String getFormatStringForResponse() {

			return "image/png";
		}

		@Override
		public String getFileExtensionWithDot() {

			return "." + this.getFileExtensionWithoutDot();

		}

		@Override
		public String getFileExtensionWithoutDot() {

			return "png";
		}

	},
	SVG {

		@Override
		public String getFormatStringForResponse() {

			return "image/svg+xml";
		}

		@Override
		public String getFileExtensionWithDot() {

			return "." + this.getFileExtensionWithoutDot();

		}

		@Override
		public String getFileExtensionWithoutDot() {

			return "svg";
		}
	};

	public abstract String getFormatStringForResponse();

	public abstract String getFileExtensionWithDot();

	public abstract String getFileExtensionWithoutDot();

	public static ImageFormat valueOfFileExtension(final String extensionWithoutDot) {

		for (ImageFormat format : ImageFormat.values()) {

			if (format.getFileExtensionWithoutDot().equals(extensionWithoutDot)) {

				return format;
			}
		}

		throw new IllegalArgumentException("unbekannte extension " + extensionWithoutDot + " - dafuer gibt es kein ImageFormat");
	}

}
