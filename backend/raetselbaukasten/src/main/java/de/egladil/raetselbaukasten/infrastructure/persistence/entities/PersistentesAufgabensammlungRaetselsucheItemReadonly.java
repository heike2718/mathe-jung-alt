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

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentesAufgabensammlungRaetselsucheItemReadonly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_RAETSEL_AUFGABENSAMMLUNGEN")
@NamedQueries({ @NamedQuery(
        name = "PersistentesAufgabensammlungRaetselsucheItemReadonly.FIND_WITH_RAETSEL_ID",
        query = "select a from PersistentesAufgabensammlungRaetselsucheItemReadonly a where a.raetselId = :raetselId order by a.sammlungName") })
public class PersistentesAufgabensammlungRaetselsucheItemReadonly {

    public static final String FIND_WITH_RAETSEL_ID = "PersistentesAufgabensammlungRaetselsucheItemReadonly.FIND_WITH_RAETSEL_ID";

    @Id
    @Column
    private String id;

    @Column(name = "SAMMLUNG_ID")
    private String sammlungId;

    @Column(name = "SAMMLUNG_NAME")
    private String sammlungName;

    @Column
    @Enumerated(EnumType.STRING)
    private Schwierigkeitsgrad schwierigkeitsgrad;

    @Column(name = "FREIGEGEBEN")
    private boolean sammlungFreigegeben;

    @Column(name = "PRIVAT")
    private boolean sammlungPrivat;

    @Column(name = "SAMMLUNG_OWNER")
    private String sammlungOwner;

    @Column(name = "ELEMENT_NUMMER")
    private String elementNummer;

    @Column(name = "ELEMENT_PUNKTE")
    private int elementPunkte;

    @Column(name = "RAETSEL_ID")
    private String raetselId;

    @Column(name = "RAETSEL_SCHLUESSEL")
    private String raetselSchluessel;

}
