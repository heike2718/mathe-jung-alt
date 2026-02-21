// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AnzahlabfrageResponseDto
 */
@Schema(name = "AnzahlabfrageResponseDto", description = "Ergebnis ist die Zahl, die die Anzahlabfrage ermittelt hat.")
public class AnzahlabfrageResponseDto {

    @JsonProperty
    private long ergebnis;

    public long getErgebnis() {

        return ergebnis;
    }

    public void setErgebnis(final long ergebnis) {

        this.ergebnis = ergebnis;
    }

}
