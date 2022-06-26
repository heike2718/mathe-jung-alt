// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

/**
 * DeskriptorEntity
 */
@Entity
@Table(name = "DESKRIPTOREN")
public class DeskriptorEntity extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "NAME")
	public String name;

	@Column(name = "ADMIN")
	public boolean admin;

	@Column(name = "KONTEXT")
	public String kontext;

	/**
	 *
	 */
	public DeskriptorEntity() {

		super();
	}

	/**
	 * @param name
	 *                  String
	 * @param adminOnly
	 *                  boolean
	 */
	public DeskriptorEntity(final String name, final boolean adminOnly, final String kontext) {

		this.name = name;
		this.admin = adminOnly;
		this.kontext = kontext;
	}
}
