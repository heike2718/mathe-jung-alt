// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * ConfigService
 */
@ApplicationScoped
public class ConfigService {

	public static final String STAGE_DEV = "dev";

	public static final String DEFAULT_ENCODING = "UTF-8";

	@ConfigProperty(name = "block.on.missing.origin.referer", defaultValue = "false")
	boolean blockOnMissingOriginReferer;

	@ConfigProperty(name = "target.origin")
	String targetOrigin;

	@ConfigProperty(name = "stage")
	String stage;

	@ConfigProperty(name = "allowedOrigin", defaultValue = "https://mathe-jung-alt.de")
	String allowedOrigin;

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

}
