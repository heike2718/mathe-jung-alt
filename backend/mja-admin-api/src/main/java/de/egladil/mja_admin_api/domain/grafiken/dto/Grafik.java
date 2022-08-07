// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.grafiken.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * Grafik
 */
public class Grafik {

	@JsonProperty
	private MessagePayload messagePayload;

	@JsonProperty
	private String pfad;

	@JsonProperty
	private byte[] image;

	public String getPfad() {

		return pfad;
	}

	public Grafik withPfad(final String pfad) {

		this.pfad = pfad;
		return this;
	}

	public MessagePayload getMessagePayload() {

		return messagePayload;
	}

	public Grafik withMessagePayload(final MessagePayload messagePayload) {

		this.messagePayload = messagePayload;
		return this;
	}

	public byte[] getImage() {

		return image;
	}

	public Grafik withImage(final byte[] image) {

		this.image = image;
		return this;
	}

}
