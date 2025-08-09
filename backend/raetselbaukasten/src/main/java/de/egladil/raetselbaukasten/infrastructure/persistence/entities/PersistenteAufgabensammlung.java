// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Referenztyp;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

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
public class PersistenteAufgabensammlung {

	public static final String FIND_BY_NAME = "PersistenteAufgabensammlung.FIND_BY_NAME";

	public static final String FIND_BY_UNIQUE_KEY = "PersistenteAufgabensammlung.FIND_BY_UNIQUE_KEY";

	@Id
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
	@Column(name = "UUID", updatable = false, nullable = false, length = 36)
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

	/**
	 * @return
	 */
	public boolean isPersistent() {

		return uuid != null;
	}
}
