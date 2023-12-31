// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import de.egladil.mja_api.domain.medien.Medienart;

/**
 * Quellenart
 */
public enum Quellenart {

	BUCH {

		@Override
		public Medienart getSuitableMedienart() {

			return Medienart.BUCH;
		}

	},
	INTERNET {

		@Override
		public Medienart getSuitableMedienart() {

			return Medienart.INTERNET;
		}

	},
	PERSON,
	ZEITSCHRIFT {

		@Override
		public Medienart getSuitableMedienart() {

			return Medienart.ZEITSCHRIFT;
		}

	};

	public Medienart getSuitableMedienart() {

		return null;
	}
}
