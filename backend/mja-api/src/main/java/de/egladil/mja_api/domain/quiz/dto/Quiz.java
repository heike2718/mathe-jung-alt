// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz.dto;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Quiz. Zusammenstellung von Rätseln zu einer Gruppe, die direkt in einer GUI dargestellt werden kann.
 */
@Schema(
	name = "Quiz",
	description = "Ein Quiz, also eine Zusammenstellung von Rätseln, die direkt in einer GUI dargestellt werden kann.")
public class Quiz {

	@JsonProperty
	@Schema(description = "der Name der Quiz")
	private String name;

	@JsonProperty
	@Schema(description = "die Klassenstufe, für die diese Quiz gedacht ist")
	private String klassenstufe;

	@JsonProperty
	@Schema(
		description = "Anzahl Punkte, mit dem man in das Quiz startet. Der Wert wird so berechnet, dass man bei 0 Punkten landet, wenn man alle Aufgaben falsch löst. Um Rundungsfehler zu vermeiden, wird mit 100 multipliziert, also 1200 statt 12.")
	private int startpunkte;

	@JsonProperty
	@Schema(type = SchemaType.ARRAY, implementation = Quizaufgabe.class)
	private List<Quizaufgabe> aufgaben;

	public String getName() {

		return name;
	}

	public Quiz withName(final String name) {

		this.name = name;
		return this;
	}

	public String getKlassenstufe() {

		return klassenstufe;
	}

	public Quiz withKlassenstufe(final String klassenstufe) {

		this.klassenstufe = klassenstufe;
		return this;
	}

	public List<Quizaufgabe> getAufgaben() {

		return aufgaben;
	}

	public void setAufgaben(final List<Quizaufgabe> aufgaben) {

		this.aufgaben = aufgaben;
	}

}
