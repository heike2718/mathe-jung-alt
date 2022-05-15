// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import java.util.List;

import de.egladil.mathe_jung_alt_ws.domain.AbstractDomainEntity;
import de.egladil.mathe_jung_alt_ws.domain.semantik.AggregateRoot;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * Raetsel
 */
@AggregateRoot
public class Raetsel extends AbstractDomainEntity {

	private String schluessel;

	private String titel;

	private String frage;

	private String loesung;

	private String kommentar;

	private String quelleId;

	private List<Antwortvorschlag> antwortvorschlaege;

	private List<Deskriptor> deskriptoren;

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof Raetsel)) {

			return false;
		}

		return super.equals(obj);
	}

}
