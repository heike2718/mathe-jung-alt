// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.health;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.exception.MjaRuntimeException;
import de.egladil.mja_admin_api.domain.utils.MjaFileUtils;
import de.egladil.mja_admin_api.infrastructure.restclient.LaTeXRestClient;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * LaTeXClientReadinessCheck
 */
@ApplicationScoped
@Readiness
public class LaTeXClientReadinessCheck implements HealthCheck {

	private static final String HEALTHCHECK_FILENAME_WITHOUT_SUFFIX = "healthCheckTest";

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXClientReadinessCheck.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Override
	public HealthCheckResponse call() {

		try {

			this.generateHelloPNG();
			return HealthCheckResponse.up("LaTeXClient is ready");
		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);

			return HealthCheckResponse.down(
				"LaTeXClient is down");
		}

	}

	void generateHelloPNG() throws MjaRuntimeException {

		File healthCheckFile = new File(latexBaseDir + File.separator + HEALTHCHECK_FILENAME_WITHOUT_SUFFIX + ".tex");

		if (!healthCheckFile.isFile() || !healthCheckFile.canRead()) {

			String template = MjaFileUtils.loadTemplate("/latex/" + HEALTHCHECK_FILENAME_WITHOUT_SUFFIX + ".tex");
			MjaFileUtils.writeOutput(healthCheckFile, template,
				"konnte " + HEALTHCHECK_FILENAME_WITHOUT_SUFFIX + ".tex nicht anlegen");

		}

		Response response = laTeXClient.latex2PNG(HEALTHCHECK_FILENAME_WITHOUT_SUFFIX);
		MessagePayload message = response.readEntity(MessagePayload.class);

		if (!message.isOk()) {

			throw new MjaRuntimeException("Hello-PNG konnte nicht generiert werden");
		}

	}

}
