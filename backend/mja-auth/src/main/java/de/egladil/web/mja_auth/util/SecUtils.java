// =====================================================
// Project: mja-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.web.mja_auth.exception.MjaAuthRuntimeException;

/**
 * SecUtils
 */
public final class SecUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecUtils.class);

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

	/**
	 * @return
	 */
	public static String generateRandomString() {

		try {

			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
			return new String(Base64.getEncoder().encode(new String("" + sr.nextLong()).getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {

			LOGGER.error(e.getMessage(), e);
			throw new MjaAuthRuntimeException(e.getMessage(), e);
		}
	}

}
