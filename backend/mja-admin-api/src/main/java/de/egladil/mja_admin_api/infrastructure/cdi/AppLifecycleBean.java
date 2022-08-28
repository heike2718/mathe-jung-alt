// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.cdi;

import java.io.File;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;

/**
 * AppLifecycleBean
 */
@ApplicationScoped
public class AppLifecycleBean {

	private static final String NAME_DOWNLOAD_DIR = "unterlagen";

	private static final String NAME_UPLOAD_DIR = "upload";

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "images.base.dir")
	String imagesBaseDir;

	@ConfigProperty(name = "quarkus.http.cors.origins", defaultValue = "")
	String corsAllowedOrigins;

	@ConfigProperty(name = "delay.milliseconds", defaultValue = "0")
	long delayMillis = 0;

	void onStartup(@Observes final StartupEvent ev) {

		checkLatexBaseDir();
		checkImagesBaseDir();

		LOGGER.info(" ===========>  the latex.base.dir is {}", latexBaseDir);
		LOGGER.info(" ===========>  the images.base.dir is {}", imagesBaseDir);
		LOGGER.info(" ===========>  quarkus.http.cors.origins={}", corsAllowedOrigins);

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

	private void checkImagesBaseDir() {

		File uploadDir = new File(imagesBaseDir);

		if (!uploadDir.exists()) {

			try {

				FileUtils.forceMkdir(uploadDir);
			} catch (IOException e) {

				LOGGER.error("Verzeichnis {} konnte nicht ereugt werden: {}", e.getMessage());
			}
		}
	}

}
