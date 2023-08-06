// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.config;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * ConfigService
 */
@ApplicationScoped
public class ConfigService {

	public static final String STAGE_DEV = "dev";

	public static final String STAGE_PROD = "prod";

	public static final String DEFAULT_ENCODING = "UTF-8";

	@ConfigProperty(name = "block.on.missing.origin.referer", defaultValue = "false")
	boolean blockOnMissingOriginReferer;

	@ConfigProperty(name = "target.origin")
	String targetOrigin;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "quarkus.http.cors.origins")
	String allowedOrigin;

	@ConfigProperty(name = "quarkus.http.cors.methods")
	String allowedMethods;

	@ConfigProperty(name = "quarkus.http.cors.headers")
	String allowedHeaders;

	@ConfigProperty(name = "quarkus.http.cors.exposed-headers")
	String exposedHeaders;

	@ConfigProperty(name = "mock.session")
	boolean mockSession = false;

	@ConfigProperty(name = "csrf.enabled")
	boolean csrfEnabled = true;

	/**
	 * @return the blockOnMissingOriginReferer
	 */
	public boolean isBlockOnMissingOriginReferer() {

		return blockOnMissingOriginReferer;
	}

	/**
	 * @return the targetOrigin
	 */
	public String getTargetOrigin() {

		return targetOrigin;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {

		return stage;
	}

	/**
	 * @return the allowedOrigin
	 */
	public String getAllowedOrigin() {

		return allowedOrigin;
	}

	/**
	 * @return the allowedMethods
	 */
	public String getAllowedMethods() {

		return allowedMethods;
	}

	/**
	 * @return the allowedHeaders
	 */
	public String getAllowedHeaders() {

		return allowedHeaders;
	}

	/**
	 * @return the exposedHeaders
	 */
	public String getExposedHeaders() {

		return exposedHeaders;
	}

	public boolean isMockSession() {

		return mockSession;
	}

	public boolean isCsrfEnabled() {

		return csrfEnabled;
	}

}
