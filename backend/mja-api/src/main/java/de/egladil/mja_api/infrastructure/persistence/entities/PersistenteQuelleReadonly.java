// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import de.egladil.mja_api.domain.quellen.Quellenart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

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
		name = "PersistenteQuelleReadonly.FIND_WITH_USER_ID",
		query = "select q from PersistenteQuelleReadonly q where q.userId = :userId")

})
public class PersistenteQuelleReadonly {

	public static final String FIND_LIKE_MEDIUM_PERSON = "PersistenteQuelleReadonly.FIND_LIKE_MEDIUM_PERSON";

	public static final String FIND_WITH_USER_ID = "PersistenteQuelleReadonly.FIND_WITH_USER_ID";

	@Id
	public String uuid;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "MEDIUM_UUID")
	public String mediumUuid;

	@Column(name = "MEDIUM_TITEL")
	public String mediumTitel;

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
	public String person;

	@Column(name = "USER_ID")
	public String userId;

}
