// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * ScanRequestPayload
 */
@Schema(description = "Payload mit einem File, das auf Viren geprüft werden soll.")
public class ScanRequestPayload {

	@JsonProperty
	@NotNull
	private String clientId;

	@JsonProperty
	private String fileOwner;

	@JsonProperty
	@NotNull
	private Upload upload;

	public String getClientId() {

		return clientId;
	}

	public ScanRequestPayload withClientId(final String clientId) {

		this.clientId = clientId;
		return this;
	}

	public String getFileOwner() {

		return fileOwner;
	}

	public ScanRequestPayload withFileOwner(final String fileOwner) {

		this.fileOwner = fileOwner;
		return this;
	}

	public Upload getUpload() {

		return upload;
	}

	public ScanRequestPayload withUpload(final Upload upload) {

		this.upload = upload;
		return this;
	}
}
