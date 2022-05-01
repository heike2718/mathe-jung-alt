// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.medien;

import de.egladil.mathe_jung_alt_ws.domain.AbstractDomainEntity;

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
