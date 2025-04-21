// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.medien;

import de.egladil.raetselbaukasten.domain.AbstractDomainEntity;

/**
 * Medium
 */
public class Medium extends AbstractDomainEntity {

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof Medium)) {

			return false;
		}

		return super.equals(obj);
	}

}
