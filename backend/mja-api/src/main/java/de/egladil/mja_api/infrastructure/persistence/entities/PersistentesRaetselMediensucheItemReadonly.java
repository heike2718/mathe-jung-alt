// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * PersistentesRaetselMediensucheItemReadonly
 */
@Entity
@Table(name = "VW_MEDIEN_RAETSEL")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesRaetselMediensucheItemReadonly.FIND_WITH_MEDIUM_ID",
		query = "select r from PersistentesRaetselMediensucheItemReadonly r where r.mediumUuid = :mediumUuid order by r.schluessel")
})
public class PersistentesRaetselMediensucheItemReadonly {

	public static final String FIND_WITH_MEDIUM_ID = "PersistentesRaetselMediensucheItemReadonly.FIND_WITH_MEDIUM_ID";

	@Id
	@Column(name = "RAETSEL_ID")
	public String uuid;

	@Column
	public String schluessel;

	@Column
	public String name;

	@Column
	public boolean freigegeben;

	@Column(name = "RAETSEL_OWNER")
	public String raetselOwner;

	@Column
	@Enumerated(EnumType.STRING)
	public RaetselHerkunftTyp herkunft;

	@Column(name = "MEDIUM_UUID")
	public String mediumUuid;

	@Column(name = "MEDIUM_TITEL")
	public String mediumTitel;

	@Column(name = "MEDIUM_ART")
	@Enumerated(EnumType.STRING)
	public Medienart medienart;

	@Column
	public String autor;

	@Column
	public String ausgabe;

	@Column
	public String jahr;

	@Column
	public String klasse;

	@Column
	public String stufe;

	@Column
	public String seite;

	@Column
	public String pfad;
}
