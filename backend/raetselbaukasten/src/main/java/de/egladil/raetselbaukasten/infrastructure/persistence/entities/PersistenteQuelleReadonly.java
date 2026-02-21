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

import de.egladil.raetselbaukasten.domain.quellen.Quellenart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistenteQuelleReadonly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VW_QUELLEN")
@NamedQueries({ @NamedQuery(
        name = "PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON",
        query = "select q from PersistenteQuelleReadonly q where q.mediumTitel like :suchstring or q.person like :suchstring order by q.sortNumber"),
        @NamedQuery(
                name = "PersistenteQuelleReadonly.FIND_WITH_USER_ID",
                query = "select q from PersistenteQuelleReadonly q where q.userId = :userId")

})
public class PersistenteQuelleReadonly {

    public static final String FIND_LIKE_MEDIUM_PERSON = "PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON";

    public static final String FIND_WITH_USER_ID = "PersistenteQuelleReadonly.FIND_WITH_USER_ID";

    @Id
    private String uuid;

    @Column(name = "ART")
    @Enumerated(EnumType.STRING)
    private Quellenart quellenart;

    @Column(name = "SORTNR")
    private long sortNumber;

    @Column(name = "MEDIUM_UUID")
    private String mediumUuid;

    @Column(name = "MEDIUM_TITEL")
    private String mediumTitel;

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
    private String person;

    @Column
    private String pfad;

    @Column(name = "USER_ID")
    private String userId;

}
