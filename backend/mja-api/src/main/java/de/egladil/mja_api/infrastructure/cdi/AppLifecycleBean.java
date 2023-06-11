// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.cdi;

import java.io.File;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "quarkus.rest-client.\"de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient\".url")
	String latexRestClientUrl;

	@ConfigProperty(name = "quarkus.rest-client.\"de.egladil.mja_api.infrastructure.restclient.InitAccessTokenRestClient\".url")

	String initAccesstokenUrl;

	@ConfigProperty(name = "quarkus.rest-client.\"de.egladil.mja_api.infrastructure.restclient.TokenExchangeRestClient\".url")
	String tokenExchangeRestClientUrl;

	@ConfigProperty(name = "quarkus.http.root-path")
	String quarkusRootPath;

	@ConfigProperty(name = "admin-redirect-url.login")
	String loginRedirectUrl;

	@ConfigProperty(name = "quarkus.http.port")
	String port;

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "")
	String corsAllowedOrigins;

	@ConfigProperty(name = "delay.milliseconds", defaultValue = "0")
	long delayMillis = 0;

	@ConfigProperty(name = "session.idle.timeout")
	int sessionIdleTimeoutMinutes = 120;

	@ConfigProperty(name = "quarkus.rest-client.\"de.egladil.mja_api.infrastructure.restclient.InitAccessTokenRestClient\".url")
	String oidcUrl;

	@ConfigProperty(name = "clamav.host")
	String clamAVHost;

	@ConfigProperty(name = "clamav.port")
	String clamAVPort;

	void onStartup(@Observes final StartupEvent ev) {

		LOGGER.info(" ===========> The application is starting with profiles" + StringUtils.join(ConfigUtils.getProfiles()));

		checkLatexBaseDir();

		LOGGER.info(" ===========>  session timeout nach {} min", sessionIdleTimeoutMinutes);
		LOGGER.info(" ===========>  the latex.base.dir is {}", latexBaseDir);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);
		LOGGER.info(" ===========>  oidcUrl={}", oidcUrl);
		LOGGER.info(" ===========>  latexRestClientUrl={}", latexRestClientUrl);
		LOGGER.info(" ===========>  initAccesstokenUrl={}", initAccesstokenUrl);
		LOGGER.info(" ===========>  tokenExchangeRestClientUrl={}", tokenExchangeRestClientUrl);
		LOGGER.info(" ===========>  port={}", port);
		LOGGER.info(" ===========>  ClamAV: host={},port={}", clamAVHost, clamAVPort);
		LOGGER.info(" ===========>  quarkusRootPath={}", quarkusRootPath);
		LOGGER.info(" ===========>  loginRedirectUrl={}", loginRedirectUrl);

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
