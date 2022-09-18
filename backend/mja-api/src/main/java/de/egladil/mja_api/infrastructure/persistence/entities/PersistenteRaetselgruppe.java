// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistenteRaetselgruppe
 */
@Entity
@Table(name = "RAETSELGRUPPEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistenteRaetselgruppe.FIND_BY_NAME",
		query = "select s from PersistenteRaetselgruppe s where s.name = :name"),
	@NamedQuery(
		name = "PersistenteRaetselgruppe.FIND_BY_UNIQUE_KEY",
		query = "select s from PersistenteRaetselgruppe s where s.referenztyp = :referenztyp and s. referenz = :referenz and s.schwierigkeitsgrad = :schwierigkeitsgrad")
})
public class PersistenteRaetselgruppe extends PanacheEntityBase implements PersistenteMjaEntity {

	public static final String FIND_BY_NAME = "PersistenteRaetselgruppe.FIND_BY_NAME";

	public static final String FIND_BY_UNIQUE_KEY = "PersistenteRaetselgruppe.FIND_BY_UNIQUE_KEY";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mja_api.infrastructure.persistence.entities.UuidGenerator")
	@NotNull
	@Size(min = 1, max = 40)
	@Column
	public String uuid;

	@Column
	public String name;

	@Column
	@Enumerated(EnumType.STRING)
	public Schwierigkeitsgrad schwierigkeitsgrad;

	@Column
	@Enumerated(EnumType.STRING)
	public Referenztyp referenztyp;

	@Column(name = "REFERENZ")
	public String referenz;

	@Column
	@Enumerated(EnumType.STRING)
	public DomainEntityStatus status;

	@Column
	public String kommentar;

	@Column(name = "GEAENDERT_DURCH")
	@NotNull
	public String geaendertDurch;

	@Column(name = "GEAENDERT_AM")
	public Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	public int version;

	@Transient
	private String importierteUuid;

	@Override
	public String getImportierteUuid() {

		return importierteUuid;
	}

	public void setImportierteUuid(final String importierteUuid) {

		this.importierteUuid = importierteUuid;
	}
}
