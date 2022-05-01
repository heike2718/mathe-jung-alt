// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.dto;

/**
 * Suchfilter
 */
public class Suchfilter {

	private final String suchstring;

	private final String deskriptorenIds;

	/**
	 * @param suchstring
	 * @param deskriptorenIds
	 */
	public Suchfilter(final String suchstring, final String deskriptorenIds) {

		this.suchstring = suchstring;
		this.deskriptorenIds = deskriptorenIds;
	}

	public String getSuchstring() {

		return suchstring;
	}

	public String getDeskriptorenIds() {

		return deskriptorenIds;
	}

}
