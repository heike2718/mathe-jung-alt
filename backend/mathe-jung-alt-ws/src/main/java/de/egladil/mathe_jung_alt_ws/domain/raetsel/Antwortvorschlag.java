// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel;

import de.egladil.mathe_jung_alt_ws.domain.semantik.ValueObject;

/**
 * Antwortvorschlag
 */
@ValueObject
public class Antwortvorschlag {

	private String buchstabe;

	private String text;

	private boolean korrekt;

}
