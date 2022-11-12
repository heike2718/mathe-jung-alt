// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

/**
 * AuthorizationUtils
 */
public final class AuthorizationUtils {

	public static final String ROLE_ADMIN = "ADMIN";

	/**
	 * Entscheidet, ob der user Änderungsrecht auf die Entity mit dem Owner ownerId hat.
	 *
	 * @param  user
	 * @param  ownerId
	 * @return         boolean
	 */
	public static boolean hasUserPermissionToChange(final String userId, final String ownerId, final boolean isAdmin) {

		if (isAdmin) {

			return true;
		}

		return userId.equals(ownerId);
	}

}
