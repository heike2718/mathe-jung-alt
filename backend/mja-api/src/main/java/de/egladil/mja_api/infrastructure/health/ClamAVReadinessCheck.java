// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import de.egladil.web.filescanner_service.clamav.ClamAVService;

/**
 * ClamAVReadinessCheck
 */
@ApplicationScoped
@Readiness
public class ClamAVReadinessCheck implements HealthCheck {

	@Inject
	ClamAVService clamAVService;

	@Override
	public HealthCheckResponse call() {

		boolean isAlive = clamAVService.checkAlive();
		return isAlive ? HealthCheckResponse.up("FileScanner is alive") : HealthCheckResponse.down("FileScanner is down");
	}
}
