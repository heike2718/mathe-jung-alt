// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import de.egladil.raetselbaukasten.domain.medien.Medienart;
import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentesRaetselMediensucheItemReadonly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_MEDIEN_RAETSEL")
@NamedQueries({ @NamedQuery(
        name = "PersistentesRaetselMediensucheItemReadonly.FIND_WITH_MEDIUM_ID",
        query = "select r from PersistentesRaetselMediensucheItemReadonly r where r.mediumUuid = :mediumUuid order by r.schluessel") })
public class PersistentesRaetselMediensucheItemReadonly {

    public static final String FIND_WITH_MEDIUM_ID = "PersistentesRaetselMediensucheItemReadonly.FIND_WITH_MEDIUM_ID";

    @Id
    @Column(name = "RAETSEL_ID")
    private String uuid;

    @Column
    private String schluessel;

    @Column
    private String name;

    @Column
    private boolean freigegeben;

    @Column(name = "RAETSEL_OWNER")
    private String raetselOwner;

    @Column
    @Enumerated(EnumType.STRING)
    private RaetselHerkunftTyp herkunft;

    @Column(name = "MEDIUM_UUID")
    private String mediumUuid;

    @Column(name = "MEDIUM_TITEL")
    private String mediumTitel;

    @Column(name = "MEDIUM_ART")
    @Enumerated(EnumType.STRING)
    private Medienart medienart;

    @Column
    private String autor;

    @Column
    private String ausgabe;

    @Column
    private String jahr;

    @Column
    private String klasse;

    @Column
    private String stufe;

    @Column
    private String seite;

    @Column
    private String pfad;
}
