// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.clientauth;

/**
 * ClientAccessTokenService
 */
public interface ClientAccessTokenService {

	/**
	 * Holt ein clientAccessToken vom authprovider
	 *
	 * @return              String das accessToken oder null!
	 */
	String orderAccessToken();

}
