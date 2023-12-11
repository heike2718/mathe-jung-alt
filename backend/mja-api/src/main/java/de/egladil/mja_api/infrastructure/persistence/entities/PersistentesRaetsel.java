// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class PersistentesRaetsel implements PersistenteMjaEntity {

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
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", type = UuidGenerator.class)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
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

	@Column
	public String deskriptoren;

	@Column
	public String kommentar;

	@Column
	@NotNull
	public String owner;

	@Column(name = "FREIGEGEBEN")
	public boolean freigegeben;

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

	@Override
	public String getImportierteUuid() {

		return importierteUuid;
	}

	public void setImportierteUuid(final String importierteUuid) {

		this.importierteUuid = importierteUuid;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isPersistent() {

		return uuid != null;
	}

}
