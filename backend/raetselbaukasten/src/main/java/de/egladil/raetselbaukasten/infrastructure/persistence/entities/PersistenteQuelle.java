// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistenteQuelle
 */
@Entity
@Table(name = "QUELLEN")
public class PersistenteQuelle {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
	@Column(name = "UUID", updatable = false, nullable = false, length = 36)
	public String uuid;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "MEDIUM")
	public String mediumID;

	@Column
	public String klasse;

	@Column
	public String stufe;

	@Column
	public String ausgabe;

	@Column
	public String jahr;

	@Column
	public String seite;

	@Column
	public String person;

	@Column
	public String pfad;

	@Column(name = "USER_ID")
	public String userId;

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

	public String getImportierteUuid() {
		return importierteUuid;
	}

	public void setImportierteUuid(String importierteUuid) {
		this.importierteUuid = importierteUuid;
	}

	/**
	 * @return
	 */
	public boolean isPersistent() {

		return uuid != null;
	}

}
