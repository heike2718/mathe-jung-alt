// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.dto;

import java.io.File;
import java.util.Base64;

import de.egladil.mja_admin_api.domain.semantik.ValueObject;
import de.egladil.web.filescanner_service.scan.Upload;

/**
 * UploadData ein Upload
 */
@ValueObject
public class UploadData {

	private final String filename;

	private byte[] data;

	private final File file;

	public UploadData(final String filename, final File file) {

		super();
		this.filename = filename;
		this.file = file;
	}

	public String getFilename() {

		return filename;
	}

	public void setData(final byte[] data) {

		this.data = data;
	}

	public byte[] getData() {

		return data;
	}

	public byte[] getDataBASE64() {

		return Base64.getEncoder().encode(data);

	}

	public Upload toUpload() {

		return new Upload().withData(data).withName(filename);

	}

	public int size() {

		return this.data.length;
	}

	public File getFile() {

		return file;
	}

}
