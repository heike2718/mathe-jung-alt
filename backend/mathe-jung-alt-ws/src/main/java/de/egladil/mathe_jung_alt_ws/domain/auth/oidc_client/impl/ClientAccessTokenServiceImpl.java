// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.auth.oidc_client.impl;

import javax.enterprise.context.RequestScoped;

import de.egladil.mathe_jung_alt_ws.domain.auth.oidc_client.ClientAccessTokenService;

/**
 * ClientAccessTokenServiceImpl
 */
@RequestScoped
public class ClientAccessTokenServiceImpl implements ClientAccessTokenService {

	@Override
	public String orderAccessToken(final String clientId, final String clientSecret) {

		return null;
	}

}
