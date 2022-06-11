// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren;

import java.util.Arrays;
import java.util.List;

/**
 * DeskriptorSuchkontext
 */
public enum DeskriptorSuchkontext {

	BILDER,
	MEDIEN,
	NOOP {

		@Override
		public boolean freigegebenFuerAlle() {

			return true;
		}

	},
	QUELLEN,
	RAETSEL {

		@Override
		public boolean freigegebenFuerAlle() {

			return true;
		}

	};

	public boolean freigegebenFuerAlle() {

		return false;
	}

	public static List<DeskriptorSuchkontext> getFreigegebe() {

		return Arrays.stream(DeskriptorSuchkontext.values()).filter((ds -> ds.freigegebenFuerAlle())).toList();

	}

}
