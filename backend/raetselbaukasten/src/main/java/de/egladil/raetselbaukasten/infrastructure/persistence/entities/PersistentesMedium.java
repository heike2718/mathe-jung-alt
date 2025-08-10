// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import de.egladil.raetselbaukasten.domain.medien.Medienart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistentesMedium
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MEDIEN")
@NamedNativeQueries({
	@NamedNativeQuery(name = PersistentesMedium.MAX_SORTNR, query = "SELECT max(m.SORTNR) from MEDIEN m", resultClass = Long.class),
	@NamedNativeQuery(name = PersistentesMedium.COUNT_ALL, query = "SELECT count(*) FROM MEDIEN", resultClass = Long.class),
	@NamedNativeQuery(name = PersistentesMedium.ANZAHL_MIT_TITEL_GLEICH, query = "SELECT count(*) from MEDIEN m where m.TITEL = :titel and m.UUID != :uuid", resultClass = Long.class),
	@NamedNativeQuery(name = PersistentesMedium.COUNT_WITH_SUCHSTRING, query = "SELECT count(*) FROM MEDIEN m WHERE m.TITEL LIKE :suchstring OR m.KOMMENTAR LIKE :suchstring", resultClass = Long.class), })
@NamedQueries({ @NamedQuery(name = PersistentesMedium.LOAD_ALL, query = "select m from PersistentesMedium m order by  m.titel"),
	@NamedQuery(name = PersistentesMedium.FIND_WITH_SUCHSTRING, query = "select m from PersistentesMedium m where m.titel like :suchstring or m.kommentar like :suchstring order by  m.titel"),
	@NamedQuery(name = PersistentesMedium.FIND_WITH_MEDIENART, query = "select m from PersistentesMedium m where m.medienart =:medienart order by m.titel"), })
public class PersistentesMedium {

	public static final String MAX_SORTNR = "PersistentesMedium.MAX_SORTNR";

	public static final String ANZAHL_MIT_TITEL_GLEICH = "PersistentesMedium.ANZAHL_MIT_TITEL_GLEICH";

	public static final String COUNT_ALL = "PersistentesMedium.COUNT_ALL";

	public static final String LOAD_ALL = "PersistentesMedium.LOAD_ALL";

	public static final String COUNT_WITH_SUCHSTRING = "PersistentesMedium.COUNT_WITH_SUCHSTRING";

	public static final String FIND_WITH_SUCHSTRING = "PersistentesMedium.FIND_WITH_SUCHSTRING";

	public static final String FIND_WITH_MEDIENART = "PersistentesMedium.FIND_WITH_MEDIENART";

	@Id
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
	@Column(name = "UUID", updatable = false, nullable = false, length = 36)
	private String uuid;

	@Column(name = "SORTNR")
	private long sortNumber;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	private Medienart medienart;

	@Column
	private String titel;

	@Column
	private String autor;

	@Column
	private String url;

	@Column
	private String kommentar;

	@Column
	private String owner;

	@Column(name = "GEAENDERT_DURCH")
	private String geaendertDurch;

	@Column(name = "GEAENDERT_AM")
	private Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	private int version;

	@Transient
	private String importierteUuid;

	/**
	 * @return
	 */
	public boolean isPersistent() {

		return uuid != null;
	}
}
