// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * Deskriptor
 */
@Entity
@Table(name = "DESKRIPTOREN")
@Schema(
	name = "Deskriptor", description = "Deskriptor-Entity mit allen Attributen.")
public class Deskriptor extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "technische ID")
	public Long id;

	@Column(name = "NAME")
	@Schema(description = "Name zum Anzeigen")
	public String name;

	@Column(name = "ADMIN")
	@Schema(description = "Flag, ob dieser Deskriptor nur in der Admin-Anwendung zur Verfügung steht")
	public boolean admin;

	@Column(name = "KONTEXT")
	@Schema(description = "Kontext des Deskriptors")
	public String kontext;

	/**
	 *
	 */
	public Deskriptor() {

		super();
	}

	/**
	 * @param name
	 *                  String
	 * @param adminOnly
	 *                  boolean
	 */
	public Deskriptor(final String name, final boolean adminOnly, final String kontext) {

		this.name = name;
		this.admin = adminOnly;
		this.kontext = kontext;
	}

	@Override
	public String toString() {

		return "Deskriptor [id=" + id + ", name=" + name + ", admin=" + admin + ", kontext=" + kontext + "]";
	}
}
