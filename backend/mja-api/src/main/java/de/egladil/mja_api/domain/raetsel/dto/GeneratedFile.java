// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GeneratedFile
 */
public class GeneratedFile {

	@JsonProperty
	private String fileName;

	@JsonProperty
	private byte[] fileData;

	/**
	 *
	 */
	public GeneratedFile() {

		super();

	}

	/**
	 * @param fileName
	 * @param fileData
	 */
	public GeneratedFile(final String fileName, final byte[] fileData) {

		super();
		this.fileName = fileName;
		this.fileData = fileData;
	}

	public String getFileName() {

		return fileName;
	}

	public void setFileName(final String filename) {

		this.fileName = filename;
	}

	public byte[] getFileData() {

		return fileData;
	}

	public void setFileData(final byte[] fileData) {

		this.fileData = fileData;
	}
}
