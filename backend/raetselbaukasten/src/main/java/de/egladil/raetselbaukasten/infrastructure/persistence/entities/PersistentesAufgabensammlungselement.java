// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentesAufgabensammlungselement
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
	private String uuid;

	@Column
	private String nummer;

	@Column(name = "SAMMLUNG")
	private String aufgabensammlungID;

	@Column(name = "RAETSEL")
	private String raetselID;

	@Column
	private int punkte;

	@Version
	@Column(name = "VERSION")
	private int version;

	/**
	 * @return
	 */
	public boolean isPersistent() {

		return uuid != null;
	}
}
