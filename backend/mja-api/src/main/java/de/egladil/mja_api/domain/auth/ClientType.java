// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * ClientType. Dient dazu, die korrekten ClientCredentials für den authprovider aus den application.properties zu laden.
 */
@Schema(description = "für das Login erforderlich")
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
