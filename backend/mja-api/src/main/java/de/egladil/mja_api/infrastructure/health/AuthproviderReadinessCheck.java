// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.HttpMethod;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.Readiness;

import io.smallrye.health.checks.UrlHealthCheck;

/**
 * AuthproviderReadinessCheck
 */
@ApplicationScoped
public class AuthproviderReadinessCheck {

	@ConfigProperty(name = "authprovider.url")
	String authProviderURL;

	@Readiness
	HealthCheck checkURL() {

		return new UrlHealthCheck(authProviderURL + "/version")
			.name("Authprovider health check").requestMethod(HttpMethod.GET).statusCode(200);
	}

}
