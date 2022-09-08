// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth;

/**
 * ClientType
 */
public enum ClientType {

	ADMIN("mja-admin"),
	PUBLIC("mja-app");

	private final String appName;

	/**
	 * @param appName
	 */
	private ClientType(final String appName) {

		this.appName = appName;
	}

	public String getAppName() {

		return appName;
	}

}
