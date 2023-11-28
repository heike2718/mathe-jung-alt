// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PersistentesRaetselgruppenelement
 */
@Entity
@Table(name = "RAETSELGRUPPENELEMENTE")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesRaetselgruppenelement.LOAD_BY_GRUPPE",
		query = "select e from PersistentesRaetselgruppenelement e where e.raetselgruppeID = :raetselgruppeID")
})
public class PersistentesRaetselgruppenelement implements PersistenteMjaEntity {

	public static final String LOAD_BY_GRUPPE = "PersistentesRaetselgruppenelement.LOAD_BY_GRUPPE";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", type = UuidGenerator.class)
	@NotNull
	@Size(min = 1, max = 40)
	@Column
	public String uuid;

	@Column
	public String nummer;

	@Column(name = "GRUPPE")
	public String raetselgruppeID;

	@Column(name = "RAETSEL")
	public String raetselID;

	@Column
	public int punkte;

	@Version
	@Column(name = "VERSION")
	public int version;

	@Transient
	private String importierteUuid;

	/**
	 * @return
	 */
	@Override
	public boolean isPersistent() {

		return uuid != null;
	}

	@Override
	public String getImportierteUuid() {

		return importierteUuid;
	}

	public void setImportierteUuid(final String importierteUuid) {

		this.importierteUuid = importierteUuid;
	}
}
