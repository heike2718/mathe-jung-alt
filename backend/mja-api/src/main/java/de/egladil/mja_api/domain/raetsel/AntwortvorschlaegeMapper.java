// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;

/**
 * AntwortvorschlaegeMapper
 */
public class AntwortvorschlaegeMapper {

	/**
	 * @param  json
	 * @return
	 */
	public static Antwortvorschlag[] deserializeAntwortvorschlaege(final String json) {

		if (json == null) {

			return new Antwortvorschlag[0];
		}

		try {

			return new ObjectMapper().readValue(json, Antwortvorschlag[].class);
		} catch (JsonProcessingException e) {

			throw new MjaRuntimeException("konnte antwortvorschlaege nicht deserialisieren: " + e.getMessage(), e);
		}
	}

	/**
	 * @param  antwortvorschlaege
	 * @return
	 */
	public static String antwortvorschlaegeAsJSON(final Antwortvorschlag[] antwortvorschlaege) {

		if (antwortvorschlaege == null) {

			return null;
		}

		try {

			return new ObjectMapper().writeValueAsString(antwortvorschlaege);
		} catch (JsonProcessingException e) {

			throw new MjaRuntimeException("konnte antwortvorschlaege nicht serialisieren: " + e.getMessage(), e);

		}
	}

}
