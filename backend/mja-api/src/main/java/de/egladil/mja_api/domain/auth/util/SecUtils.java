// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.util;

/**
 * SecUtils
 */
public final class SecUtils {

	private SecUtils() {

	}

	/**
	 * Überschreibt das char[] mit 0
	 *
	 * @param chars
	 */
	public static void wipe(final char[] chars) {

		if (chars != null) {

			for (int i = 0; i < chars.length; i++) {

				chars[i] = 0x00;
			}
		}
	}

	/**
	 * Überschreibt den String mit 0
	 *
	 * @param chars
	 */
	public static String wipe(final String str) {

		if (str != null) {

			wipe(str.toCharArray());
		}
		return null;
	}
}
