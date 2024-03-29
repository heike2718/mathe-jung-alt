// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Base64;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * UploadedFile
 */
@Schema(description = "Daten der hochgeladenen Datei: die Daten als Base64-encodeter String")
public class UploadedFile {

	@JsonProperty
	@Schema(description = "Name einer Datei", example = "bälle.png")
	@Pattern(
		regexp = MjaRegexps.VALID_DATEINAME,
		message = "Der Name der Datei enthält ungültige Zeichen. Bitte nur Ziffern, Buchstaben des deutschen Alphabets sowie die Zeichen Minus, Punkt und Unterstrich verwenden. Auch Leerzeichen sind nicht erlaubt.")
	private String name;

	@JsonProperty
	@Schema(description = "Daten der Datei Base64-Encoded", example = "ZGF0YUJhc2U2NA==")
	private String dataBase64;

	/**
	 * Gibt die BASE64-dekodierten Daten zurück.
	 *
	 * @return
	 */
	@JsonIgnore
	public byte[] getDecodedData() {

		return Base64.getDecoder().decode(dataBase64);
	}

	public String getName() {

		return name;
	}

	public UploadedFile withName(final String name) {

		this.name = name;
		return this;
	}

	public String getDataBase64() {

		return dataBase64;
	}

	/**
	 * data wird Base64-encoded und im Attribut dataBase64 abgelegt.
	 *
	 * @param  data
	 * @return      Upload
	 */
	public UploadedFile withData(final byte[] data) {

		this.dataBase64 = new String(Base64.getEncoder().encode(data));
		return this;
	}

	public void wipe() {

		if (dataBase64 != null) {

			char[] chars = dataBase64.toCharArray();

			for (int i = 0; i < chars.length; i++) {

				chars[i] = 0x00;
			}

			dataBase64 = null;
		}
	}

}
