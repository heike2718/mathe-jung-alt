// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import jakarta.persistence.*;

import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistenteAufgabeReadonly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_AUFGABEN")
@NamedQueries({ @NamedQuery(
        name = "PersistenteAufgabeReadonly.LOAD_AUFGABEN_IN_SAMMLUNG",
        query = "select a from PersistenteAufgabeReadonly a where a.sammlung = :sammlung") })
public class PersistenteAufgabeReadonly {

    public static final String LOAD_AUFGABEN_IN_SAMMLUNG = "PersistenteAufgabeReadonly.LOAD_AUFGABEN_IN_SAMMLUNG";

    @Id
    private String uuid;

    @Column
    private String schluessel;

    @Column
    private String name;

    @Column
    private String frage;

    @Column(name = "SORTNR")
    private long sortNumber;

    @Column
    private boolean freigegeben;

    @Column(name = "ANTWORTVORSCHLAEGE_EINGEBETTET")
    private boolean antwortvorschlaegeEingebettet;

    @Column
    @Enumerated(EnumType.STRING)
    private RaetselHerkunftTyp herkunft;

    @Column
    private String owner;

    @Column
    private String nummer;

    @Column
    private int punkte;

    @Column
    private String sammlung;

    @Column(name = "AUTOR_LOESUNG")
    private String autorLoesung;

    @Column
    private String antwortvorschlaege;

    @Column(name = "FILENAME_VORSCHAU_FRAGE")
    private String filenameVorschauFrage;

    @Column(name = "FILENAME_VORSCHAU_LOESUNG")
    private String filenameVorschauLoesung;

    @Column(name = "QUELLE_ART")
    @Enumerated(EnumType.STRING)
    private Quellenart quellenart;

    @Column
    private String person;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "MEDIUM_TITEL")
    private String mediumTitel;

    @Column
    private String autor;

    @Column
    private String klasse;

    @Column
    private String stufe;

    @Column
    private String jahr;

    @Column
    private String ausgabe;

    @Column
    private String seite;

    @Column(name = "DESKRIPTOREN")
    private String deskriptoren;

    @Column
    private boolean seitenumbruch;

    @Column(name = "MARGIN_BOTTOM")
    private int marginBottom;

}
