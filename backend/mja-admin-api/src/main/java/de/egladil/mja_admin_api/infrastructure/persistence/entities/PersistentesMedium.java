// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.egladil.mja_admin_api.domain.medien.Medienart;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistentesMedium
 */
public class PersistentesMedium extends PanacheEntityBase implements PersistenteMjaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mja_admin_api.infrastructure.persistence.entities.UuidGenerator")
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	public String uuid;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Medienart medienart;

	@Column
	public String titel;

	@Column
	public String deskriptoren;

	@Column
	public String pfad;

	@Column
	public String kommentar;

	@Column(name = "GEAENDERT_DURCH")
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
