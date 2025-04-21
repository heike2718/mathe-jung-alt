// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MjaImage
 */
@Schema(description = "base64 encodete Daten eines png sowie Breite und Höhe")
public class MjaImage {

	@JsonProperty
	@Schema(description = "Breite des Images")
	private int width;

	@JsonProperty
	@Schema(description = "Höhe des Images")
	private int height;

	@JsonProperty
	@Schema(
		description = "das image-Format: 'image/png' oder 'image/svg+xml'. Kann im img-Tag verwendet werden als 'data:image/png' bzw. 'data:image/svg+xml'")
	private String format;

	@JsonProperty
	@Schema(
		description = "Base64-encodetes png Kann mit einem img src=\"data:image/png;base64- Tag angezeigt werden")
	private byte[] data;

	public int getWidth() {

		return width;
	}

	public MjaImage withWidth(final int width) {

		this.width = width;
		return this;
	}

	public int getHeight() {

		return height;
	}

	public MjaImage withHeight(final int height) {

		this.height = height;
		return this;
	}

	public byte[] getData() {

		return data;
	}

	public MjaImage withData(final byte[] data) {

		this.data = data;
		return this;
	}

	public String getFormat() {

		return format;
	}

	public MjaImage withFormat(final String format) {

		this.format = format;
		return this;
	}

}
