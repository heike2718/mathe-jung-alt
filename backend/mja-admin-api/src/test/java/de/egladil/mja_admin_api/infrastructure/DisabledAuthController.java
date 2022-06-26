// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

import io.quarkus.security.spi.runtime.AuthorizationController;

/**
 * DisabledAuthController. In QuarkusTests (also ende-zu-ende-Tests) muss man die Autorisierung abschalten können.<br>
 * <br>
 * <a href="https://quarkus.io/guides/security-customization#disabling-authorization">disabling-authorization</a>
 */
@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
@ApplicationScoped
public class DisabledAuthController extends AuthorizationController {

	@Override
	public boolean isAuthorizationEnabled() {

		return false;
	}

}
