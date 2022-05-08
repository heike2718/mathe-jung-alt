// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Deskriptor
 */
@Entity
@Table(name = "DESKRIPTOREN")
public class Deskriptor extends PanacheEntity {

	@Column(name = "NAME")
	public String name;

	@Column(name = "ADMIN")
	public boolean admin;

	@Column(name = "KONTEXT")
	public String kontext;

	@Column(name = "KATEGORIE")
	@Enumerated(EnumType.STRING)
	public Deskriptorkategorie kategorie;

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
	public Deskriptor(final String name, final boolean adminOnly, final String kontext, final Deskriptorkategorie kategorie) {

		this.name = name;
		this.admin = adminOnly;
		this.kontext = kontext;
		this.kategorie = kategorie;
	}
}
