// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * LivenessCheck
 */
@Liveness
@ApplicationScoped
public class LivenessCheck implements HealthCheck {

	@Override
	public HealthCheckResponse call() {

		return HealthCheckResponse.up("mja-admin-api is up an running");
	}

}
