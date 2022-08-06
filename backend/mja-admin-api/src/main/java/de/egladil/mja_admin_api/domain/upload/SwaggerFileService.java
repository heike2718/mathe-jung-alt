// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.vertx.ConsumeEvent;

/**
 * SwaggerFileService
 */
@ApplicationScoped
public class SwaggerFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerFileService.class);

	@ConsumeEvent(blocking = true, value = "file-service")
	public void processFile(final File theFile) throws InterruptedException {

		LOGGER.info("processFile() begin");

		try {

			File tempFile = File.createTempFile("test-", ".eps");

			try (InputStream in = new FileInputStream(theFile); FileOutputStream fos = new FileOutputStream(tempFile)) {

				IOUtils.copy(in, fos);

			}

		} catch (IOException e) {

			LOGGER.error("Error", e);
		}

		LOGGER.info("processFile() end");

	}

}
