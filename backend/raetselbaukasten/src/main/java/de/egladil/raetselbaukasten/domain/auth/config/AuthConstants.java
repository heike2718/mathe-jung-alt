// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.config;

/**
 * AuthConstants
 */
public interface AuthConstants {

	final String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

	final String CSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";

	/**
	 * Dient zur Unterscheidung im Browser: Cookie wird nur an diese Sub-URL gesendet.
	 */
	final String COOKIE_PATH = "/raetselbaukasten";

}
