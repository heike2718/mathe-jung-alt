// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.clientauth;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.egladil.mja_api.domain.auth.dto.OAuthClientCredentials;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * OAuthClientCredentialsProvider
 */
@ApplicationScoped
public class OAuthClientCredentialsProvider {

	@ConfigProperty(name = "public-client-id")
	String publicClientId;

	@ConfigProperty(name = "public-client-secret")
	String publicClientSecret;

	/**
	 * @param  nonce
	 *               String, darf manchmal null sein.
	 * @return
	 */
	public OAuthClientCredentials getClientCredentials(final String nonce) {

		return OAuthClientCredentials.create(publicClientId, publicClientSecret, nonce);
	}

}
