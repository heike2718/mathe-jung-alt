// =====================================================
// Project: mja-admin-api
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.egladil.mja_api.domain.DomainEntityStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistentesRaetsel
 */
@Entity
@Table(name = "RAETSEL")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel desc")
})
public class PersistentesRaetsel extends PanacheEntityBase implements PersistenteMjaEntity {

	public static final String FIND_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN";

	public static final String FIND_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mja_api.infrastructure.persistence.entities.UuidGenerator")
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	public String uuid;

	@Column
	@NotNull
	public String schluessel;

	@Column
	@NotNull
	public String name;

	@Column
	@NotNull
	public String quelle;

	@Column
	public String deskriptoren;

	@Column
	public String kommentar;

	@Column
	@Enumerated(EnumType.STRING)
	@NotNull
	public DomainEntityStatus status;

	@Column
	@NotNull
	public String frage;

	@Column
	public String loesung;

	@Column
	public String antwortvorschlaege;

	@Column(name = "GEAENDERT_DURCH")
	@NotNull
	public String geaendertDurch;

	@Column(name = "GEAENDERT_AM")
	public Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	@JsonIgnore
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
