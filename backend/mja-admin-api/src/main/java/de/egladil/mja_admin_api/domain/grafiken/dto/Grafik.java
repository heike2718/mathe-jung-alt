// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.grafiken.dto;

import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * Grafik
 */
public class Grafik {

	private MessagePayload messagePayload;

	private String pfad;

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

}
