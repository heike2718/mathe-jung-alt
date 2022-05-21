// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * PersistentesRaetselHistorieItem
 */
@Entity
@Table(name = "HISTORIE_RAETSEL")
public class PersistentesRaetselHistorieItem extends PanacheEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	public PersistentesRaetsel raetsel;

	@Column
	public String frage;

	@Column
	public String loesung;

	@Column(name = "ANZAHL_ANTWORTEN")
	public int anzahlAntworten;

	@Column(name = "GEAENDERT_DURCH")
	public String geaendertDurch;

	@Column(name = "GEAENDERT_AM")
	public Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	public int version;
}