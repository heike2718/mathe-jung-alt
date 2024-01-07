// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * PersistentesAufgabensammlungRaetselsucheItemReadonly
 */
@Entity
@Table(name = "VW_RAETSEL_AUFGABENSAMMLUNGEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesAufgabensammlungRaetselsucheItemReadonly.FIND_WITH_RAETSEL_ID",
		query = "select a from PersistentesAufgabensammlungRaetselsucheItemReadonly a where a.raetselId = :raetselId order by a.sammlungName")
})
public class PersistentesAufgabensammlungRaetselsucheItemReadonly {

	public static final String FIND_WITH_RAETSEL_ID = "PersistentesAufgabensammlungRaetselsucheItemReadonly.FIND_WITH_RAETSEL_ID";

	@Id
	@Column
	public String id;

	@Column(name = "SAMMLUNG_ID")
	public String sammlungId;

	@Column(name = "SAMMLUNG_NAME")
	public String sammlungName;

	@Column
	@Enumerated(EnumType.STRING)
	public Schwierigkeitsgrad schwierigkeitsgrad;

	@Column(name = "FREIGEGEBEN")
	public boolean sammlungFreigegeben;

	@Column(name = "PRIVAT")
	public boolean sammlungPrivat;

	@Column(name = "SAMMLUNG_OWNER")
	public String sammlungOwner;

	@Column(name = "ELEMENT_NUMMER")
	public String elementNummer;

	@Column(name = "ELEMENT_PUNKTE")
	public int elementPunkte;

	@Column(name = "RAETSEL_ID")
	public String raetselId;

	@Column(name = "RAETSEL_SCHLUESSEL")
	public String raetselSchluessel;

}
