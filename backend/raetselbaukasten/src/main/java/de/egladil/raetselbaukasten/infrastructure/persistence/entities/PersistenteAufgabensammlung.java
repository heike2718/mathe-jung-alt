// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Referenztyp;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistenteAufgabensammlung
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUFGABENSAMMLUNGEN")
@NamedQueries({
        @NamedQuery(
                name = "PersistenteAufgabensammlung.FIND_BY_NAME",
                query = "select s from PersistenteAufgabensammlung s where s.name = :name"),
        @NamedQuery(
                name = "PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY",
                query = "select s from PersistenteAufgabensammlung s where s.referenztyp = :referenztyp and s. referenz = :referenz and s.schwierigkeitsgrad = :schwierigkeitsgrad")
})
public class PersistenteAufgabensammlung {

    public static final String FIND_BY_NAME = "PersistenteAufgabensammlung.FIND_BY_NAME";

    public static final String FIND_BY_UNIQUE_KEY = "PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY";

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "UUID", updatable = false, nullable = false, length = 36)
    private String uuid;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Schwierigkeitsgrad schwierigkeitsgrad;

    @Column
    @Enumerated(EnumType.STRING)
    private Referenztyp referenztyp;

    @Column(name = "REFERENZ")
    private String referenz;

    @Column(name = "PRIVAT")
    private boolean privat;

    @Column(name = "FREIGEGEBEN")
    private boolean freigegeben;

    @Column
    private String kommentar;

    @Column
    private String owner;

    @Column(name = "GEAENDERT_DURCH")
    private String geaendertDurch;

    @Column(name = "GEAENDERT_AM")
    private Date geaendertAm;

    @Version
    @Column(name = "VERSION")
    private int version;

    /**
     * @return
     */
    public boolean isPersistent() {

        return uuid != null;
    }
}
