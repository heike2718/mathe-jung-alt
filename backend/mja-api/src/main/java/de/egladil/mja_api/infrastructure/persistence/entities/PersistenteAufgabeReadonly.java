// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.quellen.Quellenart;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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
	@Enumerated(EnumType.STRING)
	@NotNull
	public DomainEntityStatus status;

	@Column
	public String nummer;

	@Column
	public String name;

	@Column
	public int punkte;

	@Column
	public String gruppe;

	@Column
	public String antwortvorschlaege;

	@Column(name = "QUELLE_ART")
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

	@Column(name = "DESKRIPTOREN")
	public String deskriptoren;

}
