// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.entities;

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

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistentesRaetselsammlungselement
 */
@Entity
@Table(name = "RAETSELSAMMLUNGSELEMENTE")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesRaetselsammlungselement.LOAD_BY_SAMMLUNG",
		query = "select e from PersistentesRaetselsammlungselement e where e.raetselsammlungID = :raetselsammlungID")
})
public class PersistentesRaetselsammlungselement extends PanacheEntityBase implements PersistenteMjaEntity {

	public static final String LOAD_BY_SAMMLUNG = "PersistentesRaetselsammlungselement.LOAD_BY_SAMMLUNG";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mja_admin_api.infrastructure.persistence.entities.UuidGenerator")
	@NotNull
	@Size(min = 1, max = 40)
	@Column
	public String uuid;

	@Column
	public String nummer;

	@Column(name = "SAMMLUNG")
	public String raetselsammlungID;

	@Column(name = "RAETSEL")
	public String raetselID;

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
