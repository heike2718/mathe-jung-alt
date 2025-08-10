// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel.dto;

import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselLaTeXDto die Daten, die zum Generieren erforderlich sind.
 */
public class RaetselLaTeXDto {

    private String id;

    private String schluessel;

    private String frage;

    private String loesung;

    public static RaetselLaTeXDto mapFromDB(final PersistentesRaetsel raetsel) {

        RaetselLaTeXDto result = new RaetselLaTeXDto();
        result.id = raetsel.getUuid();
        result.schluessel = raetsel.getSchluessel();
        result.frage = raetsel.getFrage();
        result.loesung = raetsel.getLoesung();
        return result;
    }

    public String getId() {

        return id;
    }

    public String getSchluessel() {

        return schluessel;
    }

    public String getFrage() {

        return frage;
    }

    public String getLoesung() {

        return loesung;
    }

}
