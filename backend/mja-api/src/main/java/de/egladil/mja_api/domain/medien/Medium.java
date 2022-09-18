// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien;

import de.egladil.mja_api.domain.AbstractDomainEntity;

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
