// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ResponsePayload
 */
public class ResponsePayload {

	@JsonProperty
	private MessagePayload message;

	@JsonProperty
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
