// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.login;

import javax.ws.rs.core.Response;

import de.egladil.web.mja_auth.ClientType;

/**
 * LoginUrlService
 */
public interface LoginUrlService {

	/**
	 * @return Response mit der autorisierten Login-Redirect-Url im Payload
	 */
	Response getLoginUrl(ClientType clientType);
}
