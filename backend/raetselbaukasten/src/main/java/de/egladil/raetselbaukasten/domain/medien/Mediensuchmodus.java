// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.medien;

/**
 * Mediensuchmodus
 */
public enum Mediensuchmodus {

    NOOP("kein suchstring, mit pagination"), SEARCHSTRING("nach titel oder kommentar mit pagination");

    private final String description;

    /**
     * @param description
     */
    private Mediensuchmodus(final String description) {

        this.description = description;
    }

    public String getDescription() {

        return description;
    }

}
