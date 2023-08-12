// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import java.util.ArrayList;
import java.util.List;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;

/**
 * PermissionUtils
 */
public final class PermissionUtils {

	public static final String ROLE_ADMIN = "ADMIN";

	public static final String ROLE_AUTOR = "AUTOR";

	public static List<String> getRelevantRoles(final AuthenticationContext authContext) {

		List<String> roles = new ArrayList<>();

		if (authContext.isUserInRole(ROLE_ADMIN)) {

			roles.add(ROLE_ADMIN);
		}

		if (authContext.isUserInRole(ROLE_AUTOR)) {

			roles.add(ROLE_AUTOR);
		}

		return roles;

	}

	/**
	 * Ermittelt, ob der user leseberechtigt für die Entity mit dem gegebenen Status ist.
	 *
	 * @param  roles
	 *                List<String> relevante Rollen
	 * @param  status
	 *                DomainEntityStatus
	 * @return        boolean
	 */
	public static boolean hasReadPermission(final List<String> roles, final DomainEntityStatus status) {

		if (roles == null) {

			return false;
		}

		if (isUserAdmin(roles) || isUserAutor(roles)) {

			return true;
		}

		return status == DomainEntityStatus.FREIGEGEBEN;
	}

	/**
	 * Ermittelt, ob der user leseberechtigt für die Entity mit der gegebenen ownerId ist.
	 *
	 * @param  userId
	 *                 String UUID des angemeldeten Users
	 * @param  roles
	 *                 List<String> relevante Rollen
	 * @param  ownerId
	 *                 String ID des Objekteigentümers
	 * @return         boolean
	 */
	public static boolean hasWritePermission(final String userId, final List<String> roles, final String ownerId) {

		if (isUserAdmin(roles)) {

			return true;
		}

		if (!isUserAutor(roles)) {

			return false;
		}

		if (ownerId == null) {

			return false;
		}

		return ownerId.equals(userId);
	}

	public static boolean isUserAdmin(final List<String> roles) {

		if (roles == null) {

			return false;
		}

		return roles.contains(ROLE_ADMIN);
	}

	public static boolean isUserAutor(final List<String> roles) {

		if (roles == null) {

			return false;
		}

		return roles.contains(ROLE_AUTOR);
	}

	public static boolean isUserOrdinary(final List<String> roles) {

		if (isUserAdmin(roles)) {

			return false;
		}

		if (isUserAutor(roles)) {

			return false;
		}

		return true;
	}

	/**
	 * Checkt, ob die Suche auf Entities mit DomainEntityStatus.FREIGEGEBEN eingeschränkt werden muss.
	 *
	 * @param  user
	 *              AuthenticatedUser
	 * @return      boolen
	 */
	public static boolean restrictSucheToFreigegeben(final List<String> roles) {

		boolean nurFreigegebene = true;

		if (PermissionUtils.isUserAdmin(roles) || PermissionUtils.isUserAutor(roles)) {

			nurFreigegebene = false;
		}

		return nurFreigegebene;
	}
}
