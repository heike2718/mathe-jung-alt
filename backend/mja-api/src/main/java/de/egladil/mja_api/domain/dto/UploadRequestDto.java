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

	private String relativerPfad;

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

	public String getRelativerPfad() {

		return relativerPfad;
	}

	public UploadRequestDto withRelativerPfad(final String relativerPfad) {

		this.relativerPfad = relativerPfad;
		return this;
	}

}
