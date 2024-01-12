// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesAufgabensammlugnselement;

/**
 * Aufgabensammlungselement
 */
@Schema(name = "Aufgabensammlungselement", description = "Element einer Aufgabensammlung")
public class Aufgabensammlungselement {

	@JsonProperty
	@Schema(description = "technische ID, 'neu' für neues Element")
	private String id;

	@JsonProperty
	@Schema(description = "Nummer als Titel der Aufgabe in der Aufgabensammlung.")
	private String nummer;

	@JsonProperty
	@Schema(description = "Punkte für dieses Rätsel, multipliziert mit 100, um Rundungsfehler zu vermeiden.")
	private int punkte;

	@JsonProperty
	@Schema(description = "fachlicher SCHLUESSEL des Rätsels, nicht Teil der persistenten Daten. Nützlich für die Anzeige")
	private String raetselSchluessel;

	@JsonProperty
	@Schema(description = "Name des Rätsels, nicht Teil der Daten des Elements. Nützlich für die Anzeige")
	private String name;

	@JsonProperty
	@Schema(description = "Loesungsbuchstabe, falls vorhanden. Kann null sein")
	private String loesungsbuchstabe;

	@JsonProperty
	@Schema(description = "Der Herkunftstyp: EIGENKREATION, ZITAT, ADAPTION")
	private RaetselHerkunftTyp herkunftstyp;

	@JsonProperty
	@Schema(description = "Ob das referenzierte Rätsel freigegeben ist.")
	private boolean freigegeben;

	/**
	 * Erzeugt ein Objekt aus den verschiedenen Daten.
	 *
	 * @param  aufgabe
	 *                 PersistenteAufgabeReadonly
	 * @param  element
	 *                 PersistentesAufgabensammlugnselement
	 * @return         Aufgabensammlungselement
	 */
	public static Aufgabensammlungselement merge(final PersistenteAufgabeReadonly aufgabe, final PersistentesAufgabensammlugnselement element) {

		Aufgabensammlungselement result = new Aufgabensammlungselement();
		result.id = element.uuid;
		result.nummer = element.nummer;
		result.punkte = element.punkte;
		result.raetselSchluessel = aufgabe.schluessel;
		result.name = aufgabe.name;
		result.herkunftstyp = aufgabe.herkunft;
		result.freigegeben = aufgabe.freigegeben;

		String antwortvorschlaegeSerialized = aufgabe.antwortvorschlaege;

		if (StringUtils.isNotBlank(antwortvorschlaegeSerialized)) {

			try {

				Antwortvorschlag[] antwortvorschlaege = new ObjectMapper().readValue(antwortvorschlaegeSerialized,
					Antwortvorschlag[].class);

				Optional<Antwortvorschlag> optKorrekt = Arrays.stream(antwortvorschlaege).filter(v -> v.isKorrekt()).findFirst();

				if (optKorrekt.isPresent()) {

					result.loesungsbuchstabe = optKorrekt.get().getBuchstabe();
				}
			} catch (JsonProcessingException e) {

				throw new RuntimeException(e.getMessage(), e);
			}
		}

		return result;
	}

	public String getNummer() {

		return nummer;
	}

	public String getId() {

		return id;
	}

	public int getPunkte() {

		return punkte;
	}

	public String getRaetselSchluessel() {

		return raetselSchluessel;
	}

	public String getName() {

		return name;
	}
}
