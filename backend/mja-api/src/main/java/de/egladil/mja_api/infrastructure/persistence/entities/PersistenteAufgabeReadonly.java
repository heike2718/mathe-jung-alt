// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import de.egladil.mja_api.domain.quellen.Quellenart;
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
 * PersistenteAufgabeReadonly
 */
@Entity
@Table(name = "VW_AUFGABEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistenteAufgabeReadonly.LOAD_AUFGABEN_IN_SAMMLUNG",
		query = "select a from PersistenteAufgabeReadonly a where a.sammlung = :sammlung")
})
public class PersistenteAufgabeReadonly {

	public static final String LOAD_AUFGABEN_IN_SAMMLUNG = "PersistenteAufgabeReadonly.LOAD_AUFGABEN_IN_SAMMLUNG";

	@Id
	public String uuid;

	@Column
	public String schluessel;

	@Column
	public String name;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column
	public boolean freigegeben;

	@Column(name = "ANTWORTVORSCHLAEGE_EINGEBETTET")
	public boolean antwortvorschlaegeEingebettet;

	@Column
	@Enumerated(EnumType.STRING)
	public RaetselHerkunftTyp herkunft;

	@Column
	public String nummer;

	@Column
	public int punkte;

	@Column
	public String sammlung;

	@Column
	public String antwortvorschlaege;

	@Column(name = "FILENAME_VORSCHAU_FRAGE")
	public String filenameVorschauFrage;

	@Column(name = "FILENAME_VORSCHAU_LOESUNG")
	public String filenameVorschauLoesung;

	@Column(name = "QUELLE_ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column
	public String person;

	@Column(name = "USER_ID")
	public String userId;

	@Column(name = "MEDIUM_TITEL")
	public String mediumTitel;

	@Column
	public String autor;

	@Column
	public String klasse;

	@Column
	public String stufe;

	@Column
	public String jahr;

	@Column
	public String ausgabe;

	@Column
	public String seite;

	@Column(name = "DESKRIPTOREN")
	public String deskriptoren;

	@Column
	private boolean seitenumbruch;

	@Column(name = "MARGIN_BOTTOM")
	private int marginBottom;

}
