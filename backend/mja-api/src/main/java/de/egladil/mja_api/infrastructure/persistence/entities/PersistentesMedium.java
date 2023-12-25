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
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PersistentesMedium
 */
@Entity
@Table(name = "MEDIEN")
@NamedNativeQueries({
	@NamedNativeQuery(
		name = "PersistentesMedium.MAX_SORTNR", query = "SELECT max(m.SORTNR) from MEDIEN m", resultClass = Long.class),
	@NamedNativeQuery(name = "PersistentesMedium.COUNT_ALL", query = "SELECT count(*) FROM MEDIEN", resultClass = Long.class),
	@NamedNativeQuery(
		name = "PersistentesMedium.ANZAHL_MIT_TITEL_GLEICH",
		query = "SELECT count(*) from MEDIEN m where m.TITEL = :titel and m.UUID != :uuid",
		resultClass = Long.class),
	@NamedNativeQuery(
		name = "PersistentesMedium.COUNT_WITH_SUCHSTRING",
		query = "SELECT count(*) FROM MEDIEN m WHERE m.TITEL LIKE :suchstring OR m.KOMMENTAR LIKE :suchstring",
		resultClass = Long.class),
})
@NamedQueries({
	@NamedQuery(
		name = "PersistentesMedium.LOAD_ALL",
		query = "select m from PersistentesMedium m order by  m.titel"),
	@NamedQuery(
		name = "PersistentesMedium.FIND_WITH_SUCHSTRING",
		query = "select m from PersistentesMedium m where m.titel like :suchstring or m.kommentar like :suchstring order by  m.titel"),
	@NamedQuery(
		name = "PersistentesMedium.FIND_BY_TITEL",
		query = "select m from PersistentesMedium m where m.titel like :suchstring order by  m.titel"),
})
public class PersistentesMedium implements PersistenteMjaEntity {

	public static final String MAX_SORTNR = "PersistentesMedium.MAX_SORTNR";

	public static final String ANZAHL_MIT_TITEL_GLEICH = "PersistentesMedium.ANZAHL_MIT_TITEL_GLEICH";

	public static final String COUNT_ALL = "PersistentesMedium.COUNT_ALL";

	public static final String LOAD_ALL = "PersistentesMedium.LOAD_ALL";

	public static final String COUNT_WITH_SUCHSTRING = "PersistentesMedium.COUNT_WITH_SUCHSTRING";

	public static final String FIND_WITH_SUCHSTRING = "PersistentesMedium.FIND_WITH_SUCHSTRING";

	public static final String FIND_BY_TITEL = "PersistentesMedium.FIND_BY_TITEL";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", type = UuidGenerator.class)
	@NotNull
	@Size(min = 1, max = 40)
	@Column
	public String uuid;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Medienart medienart;

	@Column
	public String titel;

	@Column
	public String autor;

	@Column
	public String url;

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
