// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

/**
 * UploadRequestDto
 */
public class UploadRequestDto {

	private String benutzerUuid;

	private UploadData uploadData;

	private UploadType uploadType;

	public String getBenutzerUuid() {

		return benutzerUuid;
	}

	public UploadRequestDto withBenutzerUuid(final String benutzerUuid) {

		this.benutzerUuid = benutzerUuid;
		return this;
	}

	public UploadData getUploadData() {

		return uploadData;
	}

	public UploadRequestDto withUploadData(final UploadData uploadData) {

		this.uploadData = uploadData;
		return this;
	}

	public UploadType getUploadType() {

		return uploadType;
	}

	public UploadRequestDto withUploadType(final UploadType uploadType) {

		this.uploadType = uploadType;
		return this;
	}

}
