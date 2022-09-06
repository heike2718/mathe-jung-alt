// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import de.egladil.mja_admin_api.domain.quellen.Quellenart;
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

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "PERSON")
	private String person;

	@Column(name = "MEDIUM_TITEL")
	private String mediumTitel;

	@Column(name = "JAHRGANG")
	private String jahrgang;

	@Column(name = "AUSGABE")
	private String ausgabe;

	@Column(name = "SEITE")
	private String seite;

	@Column(name = "HW")
	private boolean hw;
}
