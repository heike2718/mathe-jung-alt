// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import jakarta.persistence.*;

/**
 * PersistentesAufgabensammlungselement
 */
@Entity
@Table(name = "AUFGABENSAMMLUNGSELEMENTE")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesAufgabensammlungselement.LOAD_BY_AUFGABENSAMMLUNG",
		query = "select e from PersistentesAufgabensammlungselement e where e.aufgabensammlungID = :aufgabensammlungID")
})
public class PersistentesAufgabensammlungselement {

	public static final String LOAD_BY_AUFGABENSAMMLUNG = "PersistentesAufgabensammlungselement.LOAD_BY_AUFGABENSAMMLUNG";

	@Id
	@org.hibernate.annotations.UuidGenerator
	@Column(name = "UUID", updatable = false, nullable = false, length = 36)
	public String uuid;

	@Column
	public String nummer;

	@Column(name = "SAMMLUNG")
	public String aufgabensammlungID;

	@Column(name = "RAETSEL")
	public String raetselID;

	@Column
	public int punkte;

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
