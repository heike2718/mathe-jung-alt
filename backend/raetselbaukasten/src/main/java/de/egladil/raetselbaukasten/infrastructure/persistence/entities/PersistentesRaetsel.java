// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistentesRaetsel
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RAETSEL")
@NamedQueries({
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren order by r.schluessel"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel desc"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren order by r.schluessel desc"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel desc"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC",
                query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel desc"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL",
                query = "select r from PersistentesRaetsel r where r.schluessel = :schluessel"),
        @NamedQuery(
                name = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST",
                query = "select r from PersistentesRaetsel r where r.schluessel IN :schluessel")
})
public class PersistentesRaetsel {

    public static final String FIND_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN";

    public static final String FIND_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC";

    public static final String FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN";

    public static final String FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC";

    public static final String FIND_NOT_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN";

    public static final String FIND_NOT_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC";

    public static final String FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN";

    public static final String FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC";

    public static final String FIND_WITH_SCHLUESSEL = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL";

    public static final String FIND_WITH_SCHLUESSEL_LIST = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST";

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "UUID", updatable = false, nullable = false, length = 36)
    private String uuid;

    @Column
    private String schluessel;

    @Column
    private String name;

    @Column
    private String quelle;

    @Column(name = "AUTOR_LOESUNG")
    private String autorLoesung;

    @Column
    private String deskriptoren;

    @Column
    private String kommentar;

    @Column
    private String owner;

    @Column
    private boolean freigegeben;

    @Column(name = "ANTWORTVORSCHLAEGE_EINGEBETTET")
    private boolean antwortvorschlaegeEingebettet;

    @Column
    @Enumerated(EnumType.STRING)
    private RaetselHerkunftTyp herkunft;

    @Column
    private String frage;

    @Column
    private String loesung;

    @Column
    private String antwortvorschlaege;

    @Column(name = "FILENAME_VORSCHAU_FRAGE")
    private String filenameVorschauFrage;

    @Column(name = "FILENAME_VORSCHAU_LOESUNG")
    private String filenameVorschauLoesung;

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
