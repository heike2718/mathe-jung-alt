// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;

/**
 * GeneratedImages
 */
public class GeneratedImages {

	@JsonProperty
	private Outputformat outputFormat;

	@JsonProperty
	private String urlFrage;

	@JsonProperty
	private String urlLoesung;

	@JsonProperty
	private byte[] imageFrage;

	@JsonProperty
	private byte[] imageLoesung;

	public byte[] getImageFrage() {

		return imageFrage;
	}

	public void setImageFrage(final byte[] imageFrage) {

		this.imageFrage = imageFrage;
	}

	public byte[] getImageLoesung() {

		return imageLoesung;
	}

	public void setImageLoesung(final byte[] imageLoesung) {

		this.imageLoesung = imageLoesung;
	}

	public String getUrlFrage() {

		return urlFrage;
	}

	public void setUrlFrage(final String urlFrage) {

		this.urlFrage = urlFrage;
	}

	public String getUrlLoesung() {

		return urlLoesung;
	}

	public void setUrlLoesung(final String urlLoesung) {

		this.urlLoesung = urlLoesung;
	}

	public Outputformat getOutputFormat() {

		return outputFormat;
	}

	public void setOutputFormat(final Outputformat outputFormat) {

		this.outputFormat = outputFormat;
	}

}
