// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren;

import de.egladil.raetselbaukasten.domain.generatoren.impl.LaTeXConstants;

/**
 * TrennerartFrageLoesung
 */
public enum TrennerartFrageLoesung {

    ABSTAND {

        @Override
        public String getLeTeX() {

            return LaTeXConstants.ABSTAND_ITEMS;
        }

    },
    SEITENUMBRUCH {

        @Override
        public String getLeTeX() {

            return LaTeXConstants.VALUE_NEWPAGE;
        }
    };

    /**
     * @return String das LaTeX-Schnipsel für diese Trennerart.
     */
    public abstract String getLeTeX();
}
