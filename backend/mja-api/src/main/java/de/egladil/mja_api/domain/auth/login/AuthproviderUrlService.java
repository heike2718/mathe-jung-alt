// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login;

import jakarta.ws.rs.core.Response;

/**
 * AuthproviderUrlService
 */
public interface AuthproviderUrlService {

	/**
	 * @return Response mit der autorisierten Login-Redirect-Url im Payload
	 */
	Response getLoginUrl();

	/**
	 * @return Response mit der autorisierten Signup-Redirect-Url im Payload
	 */
	Response getSignupUrl();
}
