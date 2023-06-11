// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth;

import de.egladil.mja_api.domain.auth.ClientType;

/**
 * ClientAccessTokenService
 */
public interface ClientAccessTokenService {

	/**
	 * Holt ein clientAccessToken vom authprovider
	 *
	 * @return String das accessToken oder null!
	 */
	String orderAccessToken(ClientType clientType);

}
