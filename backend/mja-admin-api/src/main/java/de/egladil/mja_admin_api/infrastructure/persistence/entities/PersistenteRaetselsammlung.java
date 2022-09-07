// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import de.egladil.mja_admin_api.domain.DomainEntityStatus;
import de.egladil.mja_admin_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_admin_api.domain.sammlungen.Schwierigkeitsgrad;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistenteRaetselsammlung
 */
@Entity
@Table(name = "RAETSELSAMMLUNGEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistenteRaetselsammlung.FIND_BY_UNIQUE_KEY",
		query = "select s from PersistenteRaetselsammlung s where s.referenztyp = :referenztyp and s. referenz = :referenz and s.schwierigkeitsgrad = :schwierigkeitsgrad")
})
public class PersistenteRaetselsammlung extends PanacheEntityBase implements PersistenteMjaEntity {

	public static final String FIND_BY_UNIQUE_KEY = "PersistenteRaetselsammlung.FIND_BY_UNIQUE_KEY";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mja_admin_api.infrastructure.persistence.entities.UuidGenerator")
	@NotNull
	@Size(min = 1, max = 40)
	@Column
	public String uuid;

	@Column
	public String name;

	@Column
	public Schwierigkeitsgrad schwierigkeitsgrad;

	@Column
	public Referenztyp referenztyp;

	@Column(name = "REFERENZ")
	public String referenz;

	@Column
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
