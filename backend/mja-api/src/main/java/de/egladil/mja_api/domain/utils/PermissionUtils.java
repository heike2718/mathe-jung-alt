// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;

/**
 * PermissionUtils
 */
public final class PermissionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionUtils.class);

	public static final String ROLE_ADMIN = "ADMIN";

	public static final String ROLE_AUTOR = "AUTOR";

	/**
	 * Gibt die Rollen zurück, die Rätsel erstellen und editieren dürfen.
	 *
	 * @param  authContext
	 * @return
	 */
	public static List<String> getRolesWithWriteRaetselAndAufgabensammlungenPermission(final AuthenticationContext authContext) {

		List<String> roles = new ArrayList<>();

		if (authContext.isUserInRole(ROLE_ADMIN)) {

			roles.add(ROLE_ADMIN);

		}

		if (authContext.isUserInRole(ROLE_AUTOR)) {

			roles.add(ROLE_AUTOR);
		}

		LOGGER.debug("ROLES from authContext: {}", roles.stream().collect(Collectors.joining()));
		return roles;

	}

	/**
	 * Ermittelt, ob der admin leseberechtigt für die Entity mit dem gegebenen Status ist.
	 *
	 * @param  roles
	 *                List<String> relevante Rollen
	 * @param  status
	 *                DomainEntityStatus
	 * @return        boolean
	 */
	public static boolean hasReadPermission(final List<String> roles, final boolean freigegeben) {

		if (roles == null) {

			return false;
		}

		if (isUserAdmin(roles) || isUserAutor(roles)) {

			return true;
		}

		return freigegeben;
	}

	/**
	 * Ermittelt, ob der admin leseberechtigt für die Entity mit der gegebenen ownerId ist.
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

	public static boolean isUserOrdinary(final String[] roles) {

		List<String> rollen = Arrays.asList(roles);

		if (isUserAdmin(rollen)) {

			return false;
		}

		if (isUserAutor(rollen)) {

			return false;
		}

		return true;
	}

	/**
	 * Checkt, ob die Suche auf Entities mit DomainEntityStatus.FREIGEGEBEN eingeschränkt werden muss.
	 *
	 * @param  admin
	 *               AuthenticatedUser
	 * @return       boolen
	 */
	public static boolean restrictSucheToFreigegeben(final List<String> roles) {

		if (roles != null) {

			LOGGER.debug("ROLES from authContext: {}", roles.stream().collect(Collectors.joining()));
		}

		if (PermissionUtils.isUserAdmin(roles) || PermissionUtils.isUserAutor(roles)) {

			LOGGER.debug("alle Rätsel");

			return false;
		}

		LOGGER.debug("nur freigegebene Rätsel");

		return true;
	}
}
