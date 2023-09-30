// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.infrastructure.restclient.FilescannerRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * FilescannerReadinessCheck
 */
@ApplicationScoped
@Readiness
public class FilescannerReadinessCheck implements HealthCheck {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilescannerReadinessCheck.class);

	@ConfigProperty(name = "public-client-id")
	String clientId;

	@RestClient
	@Inject
	FilescannerRestClient fileScannerClient;

	@Override
	public HealthCheckResponse call() {

		Response response = fileScannerClient.ping(clientId);

		if (response.getStatus() == 200) {

			return HealthCheckResponse.up("filescanner is up");
		}

		LOGGER.error("ping filescanner answered with status={}", response.getStatus());

		return HealthCheckResponse.down("filescanner is down");

	}
}
