// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.cdi;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "quarkus.rest-client.latexrestclient.url")
	String latexRestClientUrl;

	@ConfigProperty(name = "quarkus.rest-client.filescannerrestclient.url")
	String fileScannerRestClientUrl;

	@ConfigProperty(name = "quarkus.datasource.jdbc.url")
	String jdbcUrl;

	@ConfigProperty(name = "quarkus.http.root-path")
	String quarkusRootPath;

	@ConfigProperty(name = "public-redirect-url")
	String loginRedirectUrl;

	@ConfigProperty(name = "quarkus.http.port")
	String port;

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "quarkus.http.cors.origins")
	String corsAllowedOrigins;

	@ConfigProperty(name = "quarkus.rest-client.authprovider.url")
	String authProviderUrl;

	@ConfigProperty(name = "auth-app.url")
	String authAppUrl;

	@ConfigProperty(name = "delay.milliseconds")
	long delayMillis = 0;

	@ConfigProperty(name = "session.idle.timeout")
	int sessionIdleTimeoutMinutes = 120;

	@ConfigProperty(name = "csrf.enabled")
	String csrfEnabled;

	@ConfigProperty(name = "mock.session")
	String mockSession;

	@ConfigProperty(name = "target.origin")
	String targetOrigin;

	@ConfigProperty(name = "quarkus.application.version")
	String version;

	@ConfigProperty(name = "latex.generator.preserve.tempfiles")
	boolean preserveTempFiles;

	void onStartup(@Observes
	final StartupEvent ev) {

		LOGGER.info(" ===========> Version {} of the application is starting with profiles {}", version,
			StringUtils.join(ConfigUtils.getProfiles()));

		checkLatexBaseDir();

		LOGGER.info(" ===========>  session timeout nach {} min", sessionIdleTimeoutMinutes);
		LOGGER.info(" ===========>  the latex.base.dir is {}", latexBaseDir);
		LOGGER.info(" ===========>  preserve temporary files: {}", preserveTempFiles);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  jdbcUrl={}", jdbcUrl);
		LOGGER.info(" ===========>  authAppUrl={}", authAppUrl);
		LOGGER.info(" ===========>  authProviderUrl={}", authProviderUrl);
		LOGGER.info(" ===========>  latexRestClientUrl={}", latexRestClientUrl);
		LOGGER.info(" ===========>  fileScannerRestClientUrl={}", fileScannerRestClientUrl);
		LOGGER.info(" ===========>  targetOrigin={}", targetOrigin);
		LOGGER.info(" ===========>  loginRedirectUrl={}", loginRedirectUrl);
		LOGGER.info(" ===========>  csrfEnabled={}", csrfEnabled);
		LOGGER.info(" ===========>  mockSession={}", mockSession);
		LOGGER.info(" ===========>  quarkusRootPath={}", quarkusRootPath);
		LOGGER.info(" ===========>  port={}", port);

		if (delayMillis > 0) {

			LOGGER.warn("Achtung, der Service antwortet immer erst nach {} ms!!!", delayMillis);
		}

	}

	private void checkLatexBaseDir() {

		File uploadDir = new File(latexBaseDir);

		if (!uploadDir.exists()) {

			try {

				FileUtils.forceMkdir(uploadDir);
			} catch (IOException e) {

				LOGGER.error("Verzeichnis {} konnte nicht ereugt werden: {}", e.getMessage());
			}
		}
	}

}
