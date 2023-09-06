// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth;

/**
 * ClientAccessTokenService
 */
public interface ClientAccessTokenService {

	/**
	 * Holt ein clientAccessToken vom authprovider
	 * @param nonce TODO
	 * @return String das accessToken oder null!
	 */
	String orderAccessToken(String nonce);

}
