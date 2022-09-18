// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import de.egladil.mja_api.domain.quellen.Quellenart;

/**
 * PersistenteQuelleReadonly
 */
@Entity
@Table(name = "VW_QUELLEN")
@NamedQueries({
	@NamedQuery(
		name = "PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON",
		query = "select q from PersistenteQuelleReadonly q where q.mediumTitel like :suchstring or q.person like :suchstring order by q.sortNumber"),
	@NamedQuery(
		name = "PersistenteQuelleReadonly.FIND_WITH_PERSON_EQUALS",
		query = "select q from PersistenteQuelleReadonly q where q.person = :suchstring"),
	@NamedQuery(
		name = "PersistenteQuelleReadonly.FIND_QUELLE_BY_FLAG_HW",
		query = "select q from PersistenteQuelleReadonly q where q.hw = :hw"),
	@NamedQuery(
		name = "PersistenteQuelleReadonly.FIND_WITH_DESKRIPTOREN",
		query = "select q from PersistenteQuelleReadonly q where CONCAT(CONCAT(',', q.deskriptoren),',') like :deskriptoren order by q.sortNumber")

})
public class PersistenteQuelleReadonly {

	public static final String FIND_LIKE_MEDIUM_PERSON = "PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON";

	public static final String FIND_WITH_PERSON_EQUALS = "PersistenteQuelleReadonly.FIND_WITH_PERSON_EQUALS";

	public static final String FIND_QUELLE_BY_FLAG_HW = "PersistenteQuelleReadonly.FIND_QUELLE_BY_FLAG_HW";

	public static final String FIND_WITH_DESKRIPTOREN = "PersistenteQuelleReadonly.FIND_WITH_DESKRIPTOREN";

	@Id
	public String uuid;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "PERSON")
	public String person;

	@Column(name = "MEDIUM_UUID")
	public String mediumUuid;

	@Column(name = "MEDIUM_TITEL")
	public String mediumTitel;

	@Column(name = "JAHRGANG")
	public String jahrgang;

	@Column(name = "AUSGABE")
	public String ausgabe;

	@Column(name = "SEITE")
	public String seite;

	@Column(name = "HW")
	public boolean hw;

	@Column(name = "DESKRIPTOREN")
	public String deskriptoren;

	/**
	 * @return the uuid
	 */
	public String getUuid() {

		return uuid;
	}

	/**
	 * @param uuid
	 *             the uuid to set
	 */
	public void setUuid(final String uuid) {

		this.uuid = uuid;
	}

	/**
	 * @return the quellenart
	 */
	public Quellenart getQuellenart() {

		return quellenart;
	}

	/**
	 * @param quellenart
	 *                   the quellenart to set
	 */
	public void setQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
	}

	/**
	 * @return the sortNumber
	 */
	public long getSortNumber() {

		return sortNumber;
	}

	/**
	 * @param sortNumber
	 *                   the sortNumber to set
	 */
	public void setSortNumber(final long sortNumber) {

		this.sortNumber = sortNumber;
	}

	/**
	 * @return the person
	 */
	public String getPerson() {

		return person;
	}

	/**
	 * @param person
	 *               the person to set
	 */
	public void setPerson(final String person) {

		this.person = person;
	}

	/**
	 * @return the medium
	 */
	public String getMediumTitel() {

		return mediumTitel;
	}

	/**
	 * @param medium
	 *               the medium to set
	 */
	public void setMediumTitel(final String medium) {

		this.mediumTitel = medium;
	}

	/**
	 * @return the jahrgang
	 */
	public String getJahrgang() {

		return jahrgang;
	}

	/**
	 * @param jahrgang
	 *                 the jahrgang to set
	 */
	public void setJahrgang(final String jahrgang) {

		this.jahrgang = jahrgang;
	}

	/**
	 * @return the ausgabe
	 */
	public String getAusgabe() {

		return ausgabe;
	}

	/**
	 * @param ausgabe
	 *                the ausgabe to set
	 */
	public void setAusgabe(final String ausgabe) {

		this.ausgabe = ausgabe;
	}

	/**
	 * @return the seite
	 */
	public String getSeite() {

		return seite;
	}

	/**
	 * @param seite
	 *              the seite to set
	 */
	public void setSeite(final String seite) {

		this.seite = seite;
	}

	/**
	 * @return the deskriptoren
	 */
	public String getDeskriptoren() {

		return deskriptoren;
	}

	/**
	 * @param deskriptoren
	 *                     the deskriptoren to set
	 */
	public void setDeskriptoren(final String deskriptoren) {

		this.deskriptoren = deskriptoren;
	}

	/**
	 * @return the mediumUuid
	 */
	public String getMediumUuid() {

		return mediumUuid;
	}

	/**
	 * @param mediumUuid
	 *                   the mediumUuid to set
	 */
	public void setMediumUuid(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
	}
}
