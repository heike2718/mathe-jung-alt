// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

import java.io.File;

import de.egladil.mja_api.domain.semantik.ValueObject;

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

	public File getFile() {

		return file;
	}

}
