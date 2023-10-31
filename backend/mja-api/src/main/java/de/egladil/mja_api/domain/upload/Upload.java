// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload;

import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Upload
 */
public class Upload {

	@JsonProperty
	private String name;

	@JsonProperty
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

	public Upload withName(final String name) {

		this.name = name;
		return this;
	}

	public String getDataBase64() {

		return dataBase64;
	}

	/**
	 * data wird Base64-encoded und im Attribut dataBase64 abgelegt.
	 *
	 * @param  dataNotBase64
	 * @return               Upload
	 */
	public Upload withData(final byte[] dataNotBase64) {

		this.dataBase64 = new String(Base64.getEncoder().encode(dataNotBase64));
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
