// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ResponsePayload
 */
@Schema(name = "ResponsePayload", description = "generisches Response-Objekt, das ein MessagePayload und ggf. Daten enthält")
public class ResponsePayload {

	@JsonProperty
	@Schema(description = "das MessagePayload")
	private MessagePayload message;

	@JsonProperty
	@Schema(description = "daten die als JSON mitgegeben werden. Kann null sein")
	private Object data;

	ResponsePayload() {

	}

	/**
	 * Erzeugt eine Instanz von ResponsePayload
	 */
	private ResponsePayload(final MessagePayload message) {

		super();
		this.message = message;
	}

	public MessagePayload getMessage() {

		return message;
	}

	public Object getData() {

		return data;
	}

	public void setData(final Object payload) {

		this.data = payload;
	}

	public static ResponsePayload messageOnly(final MessagePayload messagePayload) {

		return new ResponsePayload(messagePayload);
	}

}
