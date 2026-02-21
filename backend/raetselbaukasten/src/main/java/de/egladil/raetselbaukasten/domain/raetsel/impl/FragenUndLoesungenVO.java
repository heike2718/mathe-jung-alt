// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel.impl;

/**
 * FragenUndLoesungenVO
 */
public class FragenUndLoesungenVO {

    private String frageNeu;

    private String frageAlt;

    private String loesungNeu;

    private String loesungAlt;

    public String getFrageNeu() {

        return frageNeu;
    }

    public FragenUndLoesungenVO withFrageNeu(final String frageNeu) {

        this.frageNeu = frageNeu;
        return this;
    }

    public String getFrageAlt() {

        return frageAlt;
    }

    public FragenUndLoesungenVO withFrageAlt(final String frageAlt) {

        this.frageAlt = frageAlt;
        return this;
    }

    public String getLoesungNeu() {

        return loesungNeu;
    }

    public FragenUndLoesungenVO withLoesungNeu(final String loesungNeu) {

        this.loesungNeu = loesungNeu;
        return this;
    }

    public String getLoesungAlt() {

        return loesungAlt;
    }

    public FragenUndLoesungenVO withLoesungAlt(final String loesungAlt) {

        this.loesungAlt = loesungAlt;
        return this;
    }

}
