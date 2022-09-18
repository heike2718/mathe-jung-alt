// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GeneratedPDF
 */
public class GeneratedPDF {

	@JsonProperty
	private String url;

	@JsonProperty
	private String fileName;

	@JsonProperty
	private byte[] fileData;

	public String getUrl() {

		return url;
	}

	public void setUrl(final String url) {

		this.url = url;
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
