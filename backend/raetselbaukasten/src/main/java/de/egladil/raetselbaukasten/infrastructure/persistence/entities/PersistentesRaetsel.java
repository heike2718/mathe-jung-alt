// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/**
 * PersistentesRaetsel
 */
@Entity
@Table(name = "RAETSEL")
@NamedQueries({
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren order by r.schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren order by r.schluessel desc"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren order by r.schluessel desc"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel desc"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC",
		query = "select r from PersistentesRaetsel r where CONCAT(CONCAT(',', r.deskriptoren),',') not like :deskriptoren and r.freigegeben = :freigegeben order by r.schluessel desc"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL",
		query = "select r from PersistentesRaetsel r where r.schluessel = :schluessel"),
	@NamedQuery(
		name = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST",
		query = "select r from PersistentesRaetsel r where r.schluessel IN :schluessel")
})
public class PersistentesRaetsel {

	public static final String FIND_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN";

	public static final String FIND_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC";

	public static final String FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN";

	public static final String FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC";

	public static final String FIND_NOT_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN";

	public static final String FIND_NOT_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC";

	public static final String FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN";

	public static final String FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC = "PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC";

	public static final String FIND_WITH_SCHLUESSEL = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL";

	public static final String FIND_WITH_SCHLUESSEL_LIST = "PersistentesRaetsel.FIND_WITH_SCHLUESSEL_LIST";

	@Id
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
	@Column(name = "UUID", updatable = false, nullable = false, length = 36)
	public String uuid;

	@Column
	@NotNull
	public String schluessel;

	@Column
	@NotNull
	public String name;

	@Column
	@NotNull
	public String quelle;

	@Column(name = "AUTOR_LOESUNG")
	public String autorLoesung;

	@Column
	public String deskriptoren;

	@Column
	public String kommentar;

	@Column
	@NotNull
	public String owner;

	@Column
	public boolean freigegeben;

	@Column(name = "ANTWORTVORSCHLAEGE_EINGEBETTET")
	public boolean antwortvorschlaegeEingebettet;

	@Column
	@Enumerated(EnumType.STRING)
	public RaetselHerkunftTyp herkunft;

	@Column
	@NotNull
	public String frage;

	@Column
	public String loesung;

	@Column
	public String antwortvorschlaege;

	@Column(name = "FILENAME_VORSCHAU_FRAGE")
	public String filenameVorschauFrage;

	@Column(name = "FILENAME_VORSCHAU_LOESUNG")
	public String filenameVorschauLoesung;

	@Column(name = "GEAENDERT_DURCH")
	@NotNull
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
