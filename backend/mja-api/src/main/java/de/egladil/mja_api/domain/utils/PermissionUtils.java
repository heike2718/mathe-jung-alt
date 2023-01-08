// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.SecurityContext;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

/**
 * PermissionUtils
 */
public final class PermissionUtils {

	public static final String ROLE_ADMIN = "ADMIN";

	public static final String ROLE_AUTOR = "AUTOR";

	/**
	 * Sammelt den User aus dem SecurityContext. Er kann null sein.
	 *
	 * @param  securityContext
	 * @return                 AuthenticatedUser oder null!
	 */
	public static AuthenticatedUser getAuthenticatedUser(final SecurityContext securityContext) {

		Principal userPrincipal = securityContext.getUserPrincipal();

		if (userPrincipal == null) {

			return null;
		}

		return (AuthenticatedUser) userPrincipal;
	}

	/**
	 * Entscheidet, ob der user Änderungsrecht auf die Entity mit dem Owner ownerId hat.
	 *
	 * @param  user
	 * @param  ownerId
	 * @return         boolean
	 */
	@Deprecated
	public static boolean hasUserPermissionToChange(final String userId, final String ownerId, final boolean isAdmin) {

		if (isAdmin) {

			return true;
		}

		return userId.equals(ownerId);
	}

	/**
	 * Ermittelt, ob der user schreibberechtigt eine Entity mit der gegebenen ownerId ist.
	 *
	 * @param  user
	 * @param  ownerId
	 * @return
	 */
	public static boolean hasWritePermission(final AuthenticatedUser user, final String ownerId) {

		if (user == null) {

			return false;
		}

		if (isUserAdmin(user)) {

			return true;
		}

		if (!isUserAutor(user)) {

			return false;
		}

		return user.getUuid().equals(ownerId);
	}

	/**
	 * Ermittelt, ob der user leseberechtigt für eine Entity mit dem gegebenen Status ist.
	 *
	 * @param  user
	 * @param  ownerId
	 * @return
	 */
	public static boolean hasReadPermission(final AuthenticatedUser user, final DomainEntityStatus status) {

		if (user == null) {

			return false;
		}

		if (isUserAdmin(user) || isUserAutor(user)) {

			return true;
		}

		return status == DomainEntityStatus.FREIGEGEBEN;
	}

	public static boolean isUserAdmin(final AuthenticatedUser user) {

		if (user == null) {

			return false;
		}

		List<String> roles = Arrays.asList(user.getRoles());

		return roles.contains(ROLE_ADMIN);
	}

	public static boolean isUserAutor(final AuthenticatedUser user) {

		if (user == null) {

			return false;
		}

		List<String> roles = Arrays.asList(user.getRoles());

		return roles.contains(ROLE_AUTOR);
	}

	public static boolean isUserOrdinary(final AuthenticatedUser user) {

		if (isUserAdmin(user)) {

			return false;
		}

		if (isUserAutor(user)) {

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
	public static boolean restrictSucheToFreigegeben(final AuthenticatedUser user) {

		boolean nurFreigegebene = true;

		if (PermissionUtils.isUserAdmin(user) || PermissionUtils.isUserAutor(user)) {

			nurFreigegebene = false;
		}

		return nurFreigegebene;
	}
}
