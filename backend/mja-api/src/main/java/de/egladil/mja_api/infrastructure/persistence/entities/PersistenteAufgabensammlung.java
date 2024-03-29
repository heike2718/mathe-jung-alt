// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * PersistenteAufgabensammlung
 */
@Entity
@Table(name = "AUFGABENSAMMLUNGEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistenteAufgabensammlung.FIND_BY_NAME",
		query = "select s from PersistenteAufgabensammlung s where s.name = :name"),
	@NamedQuery(
		name = "PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY",
		query = "select s from PersistenteAufgabensammlung s where s.referenztyp = :referenztyp and s. referenz = :referenz and s.schwierigkeitsgrad = :schwierigkeitsgrad")
})
public class PersistenteAufgabensammlung implements PersistenteMjaEntity {

	public static final String FIND_BY_NAME = "PersistenteAufgabensammlung.FIND_BY_NAME";

	public static final String FIND_BY_UNIQUE_KEY = "PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", type = UuidGenerator.class)
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

	@Column(name = "PRIVAT")
	public boolean privat;

	@Column(name = "FREIGEGEBEN")
	public boolean freigegeben;

	@Column
	public String kommentar;

	@Column
	@NotNull
	public String owner;

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
