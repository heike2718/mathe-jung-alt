// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.clientauth;

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
