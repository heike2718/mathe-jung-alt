// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.clientauth;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.web.mja_auth.ClientType;
import de.egladil.web.mja_auth.dto.OAuthClientCredentials;

/**
 * OAuthClientCredentialsProvider
 */
@ApplicationScoped
public class OAuthClientCredentialsProvider {

	@ConfigProperty(name = "admin-client-id")
	String adminClientId;

	@ConfigProperty(name = "admin-client-secret")
	String adminClientSecret;

	@ConfigProperty(name = "public-client-id")
	String publicClientId;

	@ConfigProperty(name = "public-client-secret")
	String publicClientSecret;

	/**
	 * @param  clientType
	 *                    ClientType
	 * @param  nonce
	 *                    String, darf manchmal null sein.
	 * @return
	 */
	public OAuthClientCredentials getClientCredentials(final ClientType clientType, final String nonce) {

		switch (clientType) {

			case ADMIN:

				return OAuthClientCredentials.create(adminClientId, adminClientSecret, nonce);

			case PUBLIC:
				return OAuthClientCredentials.create(publicClientId, publicClientSecret, nonce);

			default:
				break;
		}

		throw new IllegalArgumentException("unerwarteter ClientType " + clientType);
	}

}
