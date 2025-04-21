// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.persistence.entities;

/**
 * PersistenteMjaEntity
 */
public interface PersistenteMjaEntity {

	String getImportierteUuid();

	/**
	 * @return boolean true, wenn es eine ID hat, sonst false. Dann ist es noch transient.
	 */
	boolean isPersistent();

}
