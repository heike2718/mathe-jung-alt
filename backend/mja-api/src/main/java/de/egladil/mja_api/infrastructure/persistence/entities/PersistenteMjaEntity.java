// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.entities;

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
