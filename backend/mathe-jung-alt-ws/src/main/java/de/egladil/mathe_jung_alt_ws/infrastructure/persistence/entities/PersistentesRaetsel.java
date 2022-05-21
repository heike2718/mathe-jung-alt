// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import de.egladil.web.commons_validation.annotations.UuidString;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * PersistentesRaetsel
 */
@Entity
@Table(name = "RAETSEL")
public class PersistentesRaetsel extends PanacheEntityBase implements PersistenteMjaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid_generator")
	@GenericGenerator(
		name = "uuid_generator", strategy = "de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.UuidGenerator")
	@UuidString
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "UUID")
	public String uuid;

	@Column
	public String schluessel;

	@Column
	public String name;

	@Column
	public String quelle;

	@Column
	public String deskriptoren;

	@Column
	public String kommentar;

	@Column
	public String frage;

	@Column
	public String loesung;

	@Column
	public String antwortvorschlaege;

	@Column(name = "ANZAHL_ANTWORTEN")
	public int anzahlAntworten;

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

	@Override
	public String getImportierteUuid() {

		return importierteUuid;
	}

	public void setImportierteUuid(final String importierteUuid) {

		this.importierteUuid = importierteUuid;
	}

}
