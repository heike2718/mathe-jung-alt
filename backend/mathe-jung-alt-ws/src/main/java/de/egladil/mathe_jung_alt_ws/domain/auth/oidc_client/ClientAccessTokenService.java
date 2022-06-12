// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.auth.oidc_client;

/**
 * ClientAccessTokenService kommuniziert als oidc-Client mit dem authprovider.
 */
public interface ClientAccessTokenService {

	/**
	 * Holt ein clientAccessToken vom authprovider
	 *
	 * @param  clientId
	 *                      String die ID der Webanwendung
	 * @param  clientSecret
	 *                      das Passwort der Webanwendung
	 * @return              String das accessToken oder null!
	 */
	String orderAccessToken(final String clientId, final String clientSecret);

}
