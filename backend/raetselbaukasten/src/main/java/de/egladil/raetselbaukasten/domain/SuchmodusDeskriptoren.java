// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain;

/**
 * SuchmodusDeskriptoren
 */
public enum SuchmodusDeskriptoren {

    LIKE, NOT_LIKE;

    public static SuchmodusDeskriptoren getDefault() {

        return SuchmodusDeskriptoren.LIKE;
    }
}
