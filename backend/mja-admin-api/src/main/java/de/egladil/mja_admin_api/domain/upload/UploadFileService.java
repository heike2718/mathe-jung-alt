// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.dto.UploadData;
import de.egladil.web.mja_shared.exceptions.MjaRuntimeException;

/**
 * UploadFileService
 */
@ApplicationScoped
public class UploadFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileService.class);

	// @ConsumeEvent(blocking = true, value = "upload-file-service")
	public void processFile(final UploadData uploadData) {

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