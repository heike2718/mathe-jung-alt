// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentesRaetselHistorieItem
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HISTORIE_RAETSEL")
public class PersistentesRaetselHistorieItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    private PersistentesRaetsel raetsel;

    @Column
    private String frage;

    @Column
    private String loesung;

    @Column(name = "GEAENDERT_DURCH")
    private String geaendertDurch;

    @Column(name = "GEAENDERT_AM")
    private Date geaendertAm;

    @Version
    @Column(name = "VERSION")
    private int version;
}
