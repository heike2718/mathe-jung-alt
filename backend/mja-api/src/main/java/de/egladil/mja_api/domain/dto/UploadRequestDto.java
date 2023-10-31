// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

import de.egladil.mja_api.domain.upload.Upload;

/**
 * UploadRequestDto
 */
public class UploadRequestDto {

	private String benutzerUuid;

	private Upload upload;

	private String relativerPfad;

	public String getBenutzerUuid() {

		return benutzerUuid;
	}

	public UploadRequestDto withBenutzerUuid(final String benutzerUuid) {

		this.benutzerUuid = benutzerUuid;
		return this;
	}

	public Upload getUpload() {

		return upload;
	}

	public UploadRequestDto withUpload(final Upload uploadData) {

		this.upload = uploadData;
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
