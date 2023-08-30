// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz.dto;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.dto.Images;

/**
 * Quizaufgabe.
 */
@Schema(
	name = "Quizaufgabe",
	description = "Ein Rätsel, also ein Element eines Quiz zum Anzeigen in einer GUI.")
public class Quizaufgabe {

	@JsonProperty
	@Schema(description = "Nummer der Quizaufgabe. Ist eindeutig innerhalb der Quiz und streng aufsteigend sortierbar")
	private String nummer;

	@JsonProperty
	@Schema(description = "fachlich eindeutiger Schlüssel des verwendeten Rätsels")
	private String schluessel;

	@JsonProperty
	@Schema(description = "Status des verwendeten Rätsels")
	private DomainEntityStatus status;

	@JsonProperty
	@Schema(description = "Quelle der Quizaufgabe für eine Zitatsection")
	private String quelle;

	@JsonProperty
	@Schema(
		description = "Anzahl Punkte, die es für diese Aufgabe bei gibt. Um Rundungsfehler zu vermeiden, wird mit 100 mulzipliziert, also 300 statt 3")
	private int punkte;

	@JsonProperty
	@Schema(
		description = "Anzahl Strafpunkte, die man bei einer falschen Antwort bekommt. Um Rundungsfehler zu vermeiden, wird mit 100 multipliziert, also 75 statt 0,75")
	private int strafpunkte;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = Antwortvorschlag.class,
		description = "optionale Antwortvorschläge, wenn es sich um beim Quiz um multiple choice handelt")
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	@Schema(description = "Images, die angezeigt werden können")
	Images images;

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((nummer == null) ? 0 : nummer.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (!(obj instanceof Quizaufgabe)) {

			return false;
		}
		Quizaufgabe other = (Quizaufgabe) obj;

		if (nummer == null) {

			if (other.nummer != null) {

				return false;
			}
		} else if (!nummer.equals(other.nummer)) {

			return false;
		}
		return true;
	}

	public String getNummer() {

		return nummer;
	}

	public void setNummer(final String nummer) {

		this.nummer = nummer;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public void setSchluessel(final String schluessel) {

		this.schluessel = schluessel;
	}

	public String getQuelle() {

		return quelle;
	}

	public void setQuelle(final String quelle) {

		this.quelle = quelle;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public void setAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
	}

	public Images getImages() {

		return images;
	}

	public void setImages(final Images images) {

		this.images = images;
	}

	public int getPunkte() {

		return punkte;
	}

	public void setPunkte(final int punkte) {

		this.punkte = punkte;
	}

	public int getStrafpunkte() {

		return strafpunkte;
	}

	public void setStrafpunkte(final int strafpunkte) {

		this.strafpunkte = strafpunkte;
	}

	public DomainEntityStatus getStatus() {

		return status;
	}

	public void setStatus(final DomainEntityStatus status) {

		this.status = status;
	}

}
