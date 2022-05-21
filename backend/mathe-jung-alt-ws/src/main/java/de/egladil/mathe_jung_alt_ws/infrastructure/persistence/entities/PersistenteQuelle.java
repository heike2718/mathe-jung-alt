// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.egladil.mathe_jung_alt_ws.domain.quellen.Quellenart;
import de.egladil.web.commons_validation.annotations.UuidString;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

/**
 * PersistenteQuelle
 */
@Entity
@Table(name = "QUELLEN")
public class PersistenteQuelle extends PanacheEntityBase implements PersistenteMjaEntity {

	private static final String ICH = "Heike Winkelvoß";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.UuidGenerator")
	@UuidString
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	public String uuid;

	@Column(name = "SORTNR")
	public long sortNumber;

	@Column(name = "ART")
	@Enumerated(EnumType.STRING)
	public Quellenart quellenart;

	@Column(name = "MEDIUM_UUID")
	public String mediumUuid;

	@Column(name = "JAHRGANG")
	public String jahrgang;

	@Column(name = "AUSGABE")
	public String ausgabe;

	@Column(name = "SEITE")
	public String seite;

	@Column(name = "PERSON")
	public String person;

	@Column(name = "DESKRIPTOREN")
	public String deskriptoren;

	@Column(name = "GEAENDERT_DURCH")
	public String geaendertDurch;

	@Column(name = "GEAENDERT_AM")
	public Date geaendertAm;

	@Version
	@Column(name = "VERSION")
	@JsonIgnore
	public int version;

	@Transient
	private String importierteUuid;

	public static PersistenteQuelle getDefaultQuelle() {

		Parameters params = Parameters.with("person", ICH);

		return (PersistenteQuelle) PersistenteQuelle.find("person = :person", params);
	}

	public void setImportierteUuid(final String importierteUuid) {

		this.importierteUuid = importierteUuid;
	}

	@Override
	public String getImportierteUuid() {

		return importierteUuid;
	}

}
