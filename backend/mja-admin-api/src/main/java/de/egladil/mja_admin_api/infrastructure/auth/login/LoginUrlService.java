// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.login;

import javax.ws.rs.core.Response;

/**
 * LoginUrlService
 */
public interface LoginUrlService {

	/**
	 * @return Response mit der autorisierten Login-Redirect-Url im Payload
	 */
	Response getLoginUrl();
}
