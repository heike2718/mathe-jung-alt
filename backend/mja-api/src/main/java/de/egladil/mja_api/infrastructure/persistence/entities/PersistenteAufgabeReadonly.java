// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import de.egladil.mja_api.domain.quellen.Quellenart;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistenteAufgabeReadonly
 */
@Entity
@Table(name = "VW_AUFGABEN")
public class PersistenteAufgabeReadonly extends PanacheEntityBase {

	@Id
	public String uuid;

	@Column
	public String schluessel;

	@Column
	public String antwortvorschlaege;

	@Column
	public int punkte;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "PERSON")
	public String person;

	@Column(name = "MEDIUM_TITEL")
	public String mediumTitel;

	@Column(name = "JAHRGANG")
	public String jahrgang;

	@Column(name = "AUSGABE")
	public String ausgabe;

	@Column(name = "SEITE")
	public String seite;

	@Column(name = "HW")
	public boolean hw;

	@Column(name = "DESKRIPTOREN")
	public String deskriptoren;

}
