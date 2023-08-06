// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;

/**
 * ProcessUploadService
 */
@ApplicationScoped
public class ProcessUploadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUploadService.class);

	// @ConsumeEvent(blocking = true, value = "upload-file-service")
	public void processFile(final UploadData uploadData) throws MjaRuntimeException {

		File file = uploadData.getFile();

		try (InputStream in = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

			IOUtils.copy(in, bos);

			uploadData.setData(bos.toByteArray());

			LOGGER.debug("FileInputStream gelesen");

		} catch (IOException e) {

			throw new MjaRuntimeException("Exception beim Verarbeiten des hochgeladenen Files: " + e.getMessage(), e);
		}

	}

}
