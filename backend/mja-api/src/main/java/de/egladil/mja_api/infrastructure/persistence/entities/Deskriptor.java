// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Deskriptor
 */
@Entity
@Table(name = "DESKRIPTOREN")
@NamedQueries({
	@NamedQuery(name = "Deskriptor.LIST_ALL", query = "select d from Deskriptor d order by d.id")
})
@Schema(
	name = "Deskriptor", description = "Deskriptor-Entity mit allen Attributen.")
public class Deskriptor {

	public static final String LIST_ALL = "Deskriptor.LIST_ALL";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "technische ID")
	public Long id;

	@Column(name = "NAME")
	@Schema(description = "Name zum Anzeigen", example = "Minikänguru")
	public String name;

	@Column(name = "ADMIN")
	@Schema(description = "Flag, ob dieser Deskriptor nur in der Admin-Anwendung zur Verfügung steht")
	public boolean admin;

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
	public Deskriptor(final String name, final boolean adminOnly) {

		this.name = name;
		this.admin = adminOnly;
	}

	@Override
	public String toString() {

		return "Deskriptor [id=" + id + ", name=" + name + ", admin=" + admin + "]";
	}

}
