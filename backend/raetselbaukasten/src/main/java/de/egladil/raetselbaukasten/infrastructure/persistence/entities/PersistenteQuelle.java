// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistenteQuelle
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QUELLEN")
public class PersistenteQuelle {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "UUID", updatable = false, nullable = false, length = 36)
    private String uuid;

    @Column(name = "SORTNR")
    private long sortNumber;

    @Column(name = "ART")
    @Enumerated(EnumType.STRING)
    private Quellenart quellenart;

    @Column(name = "MEDIUM")
    private String mediumID;

    @Column
    private String klasse;

    @Column
    private String stufe;

    @Column
    private String ausgabe;

    @Column
    private String jahr;

    @Column
    private String seite;

    @Column
    private String person;

    @Column
    private String pfad;

    @Column(name = "USER_ID")
    private String userId;

    @Column
    private String owner;

    @Column(name = "GEAENDERT_DURCH")
    private String geaendertDurch;

    @Column(name = "GEAENDERT_AM")
    private Date geaendertAm;

    @Version
    @Column(name = "VERSION")
    private int version;

    @Transient
    private String importierteUuid;

    /**
     * @return
     */
    public boolean isPersistent() {

        return uuid != null;
    }

}
