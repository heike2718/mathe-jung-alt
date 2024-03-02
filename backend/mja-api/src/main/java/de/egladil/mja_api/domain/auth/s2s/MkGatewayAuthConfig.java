// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.s2s;

import io.smallrye.config.ConfigMapping;

/**
 * MkGatewayAuthConfig gruppiert die Konfigurationsparameter mit dem Präfix mk-gateway.auth
 */
@ConfigMapping(prefix = "mkgateway.auth")
public interface MkGatewayAuthConfig {

	String client();

	String header();
}
