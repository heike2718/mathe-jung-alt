// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.egladil.mja_api.domain.medien.Medienart;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PersistentesMedium
 */
public class PersistentesMedium implements PersistenteMjaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", type = UuidGenerator.class)
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

	@Column
	@NotNull
	public String owner;

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
