// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.domain.medien.Medienart;

/**
 * Quellenart
 */
public enum Quellenart {

    BUCH {

        @Override
        public Medienart getSuitableMedienart() {

            return Medienart.BUCH;
        }

    },
    INTERNET {

        @Override
        public Medienart getSuitableMedienart() {

            return Medienart.INTERNET;
        }

    },
    PERSON, ZEITSCHRIFT {

        @Override
        public Medienart getSuitableMedienart() {

            return Medienart.ZEITSCHRIFT;
        }

    };

    public Medienart getSuitableMedienart() {

        return null;
    }
}
