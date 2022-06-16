// =====================================================
// Projekt: commons-validation
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.mja_admin_api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message
 */
public class Message {

	@JsonProperty
	private String level;

	@JsonProperty
	private String message;

	/**
	 * Erzeugt eine Instanz von Message
	 */
	public Message() {

	}

	/**
	 * Erzeugt eine Instanz von Message
	 */
	private Message(final String level, final String message) {

		super();
		this.level = level;
		this.message = message;
	}

	@Override
	public String toString() {

		return "Message [level=" + level + ", message=" + message + "]";
	}

	public String getLevel() {

		return level;
	}

	void setLevel(final String level) {

		this.level = level;
	}

	public String getMessage() {

		return message;
	}

	void setMessage(final String message) {

		this.message = message;
	}

	@JsonIgnore
	public boolean isOk() {

		return "INFO".equals(level);
	}

	/**
	 * INFO-MessagePalyod mit Text 'ok'.
	 *
	 * @return
	 */
	public static Message ok() {

		return info("ok");
	}

	public static Message info(final String message) {

		return new Message("INFO", message);
	}

	public static Message warn(final String message) {

		return new Message("WARN", message);
	}

	public static Message error(final String message) {

		return new Message("ERROR", message);
	}

}
