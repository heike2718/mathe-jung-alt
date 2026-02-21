// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel;

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
