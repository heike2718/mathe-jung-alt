// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_api.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * Deskriptor
 */
@Entity
@Table(name = "DESKRIPTOREN")
public class Deskriptor extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "NAME")
	public String name;

	@Column(name = "ADMIN")
	public boolean admin = false;

	@Column(name = "KONTEXT")
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
	public Deskriptor(final String name, final String kontext) {

		this.name = name;
		this.kontext = kontext;
	}

	@Override
	public String toString() {

		return "Deskriptor [id=" + id + ", name=" + name + ", admin=" + admin + ", kontext=" + kontext + "]";
	}
}
