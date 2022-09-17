// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	/**
	 * Erzeugt eine Instanz von ResponsePayload
	 */
	public ResponsePayload() {

	}

	/**
	 * Erzeugt eine Instanz von ResponsePayload
	 */
	private ResponsePayload(final MessagePayload message) {

		super();
		this.message = message;
	}

	/**
	 * Erzeugt eine Instanz von ResponsePayload
	 */
	public ResponsePayload(final MessagePayload message, final Object payload) {

		super();
		this.message = message;
		this.data = payload;
	}

	public MessagePayload getMessage() {

		return message;
	}

	public void setMessage(final MessagePayload message) {

		this.message = message;
	}

	public Object getData() {

		return data;
	}

	public void setData(final Object payload) {

		this.data = payload;
	}

	@JsonIgnore
	public boolean isOk() {

		return this.message.isOk();
	}

	public static ResponsePayload messageOnly(final MessagePayload messagePayload) {

		return new ResponsePayload(messagePayload);
	}

}
