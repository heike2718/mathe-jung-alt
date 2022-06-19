// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.login;

/**
 * TokenExchangeService
 */
public interface TokenExchangeService {

	/**
	 * Tauscht ein accessToken gegen ein JWT aus.
	 *
	 * @param  clientId
	 * @param  clientSecret
	 * @param  oneTimeToken
	 * @return              String das jwt
	 */
	String exchangeTheOneTimeToken(final String clientId, final String clientSecret, final String oneTimeToken);

}
