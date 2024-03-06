// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * QuelleDto
 */
@Schema(
	name = "Quelle",
	description = "Daten einer Quelle für ein Raetsel. Nicht alle Attribute zusammen sind sinnvoll. Eingie schließen einander aus.")
public class QuelleDto {

	@Schema(description = "technische ID, 'neu' für neue Quellen", example = "a8ee023a-3e99-4a73-8a2f-55c7f7033a9e")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält ungültige Zeichen")
	private String id;

	@Schema(description = "Art der Quelle: Mensch, Buch, Zeitschrift", required = true)
	@NotNull(message = "quellenart ist erforderlich")
	private Quellenart quellenart;

	@Schema(
		description = "Falls es sich z.B. um einen Wettbewerb handelte, ist dies der Text, der die Klasse beschreibt.",
		example = "Klasse 2")
	@Pattern(regexp = MjaRegexps.VALID_KLASSE_STUFE, message = "klasse enthält ungültige Zeichen")
	@Size(max = 20, message = "klasse darf höchstens 20 Zeichen lang sein")
	private String klasse;

	@Schema(
		description = "Falls es sich z.B. um einen Wettbewerb handelte, ist dies der Text, der die Stufe beschreibt.",
		example = "Stufe 1")
	@Pattern(regexp = MjaRegexps.VALID_KLASSE_STUFE, message = "stufe enthält ungültige Zeichen")
	@Size(max = 10, message = "stufe darf höchstens 10 Zeichen lang sein")
	private String stufe;

	@Schema(description = "Bei einer Zeitschrift die Nummer der Ausgabe.", example = "11")
	@Pattern(regexp = MjaRegexps.VALID_AUSGABE, message = "ausgabe enthält ungültige Zeichen")
	@Size(max = 10, message = "ausgabe darf höchstens 10 Zeichen lang sein")
	private String ausgabe;

	@Schema(
		description = "Bei einer Zeitschrift, das Erscheinungsjahr, bei einem Wettbewerb das Wettbewerbsjahr.", example = "1976")
	@Pattern(regexp = MjaRegexps.VALID_JAHR, message = "jahr enthält ungültige Zeichen")
	@Size(max = 4, message = "jahr darf höchstens 4 Zeichen lang sein")
	private String jahr;

	@Schema(description = "Bei einem Buch oder einer Zeitschrift die Seite", example = "32")
	@Pattern(regexp = MjaRegexps.VALID_SEITE, message = "seite enthält ungültige Zeichen")
	@Size(max = 10, message = "seite darf höchstens 10 Zeichen lang sein")
	private String seite;

	@Schema(description = "Wenn es eine bekannte Person ist, deren vollständiger Name", example = "David Hilbert")
	@Pattern(regexp = MjaRegexps.VALID_PERSON, message = "person enthält ungültige Zeichen")
	@Size(max = 100, message = "person darf höchstens 100 Zeichen lang sein")
	private String person;

	@Schema(
		description = "Pfad zu einer Datei im eigenen Filesystem, in dem die Vorlage der Aufgabe oder die Aufgabe steht.",
		example = "/mathe/aufgabensammlungen/buch.pdf")
	@Pattern(regexp = MjaRegexps.VALID_PFAD, message = "pfad enthält ungültige Zeichen")
	@Size(max = 255, message = "pfad darf höchstens 255 Zeichen lang sein")
	private String pfad;

	@Schema(
		description = "Referenz auf ein Buch, eine Zeitschrift oder etwas im Internet, falls es keine Person ist",
		example = "8df348e0-2c26-4a25-a1ab-b4840b87ae01")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "mediumUuid enthält ungültige Zeichen")
	private String mediumUuid;

	public static QuelleDto createNewForAutor(final AuthenticatedUser user) {

		QuelleDto result = new QuelleDto();
		result.quellenart = Quellenart.PERSON;
		result.person = user.getFullName();
		return result;
	}

	@Override
	public String toString() {

		return "QuelleDto [quellenart=" + quellenart + ", klasse=" + klasse + ", stufe=" + stufe + ", ausgabe=" + ausgabe
			+ ", jahr=" + jahr + ", seite=" + seite + ", person=" + person + ", pfad=" + pfad + ", mediumUuid=" + mediumUuid + "]";
	}

	public String getId() {

		return id;
	}

	public void setId(final String id) {

		this.id = id;
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public void setQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
	}

	public String getKlasse() {

		return klasse;
	}

	public void setKlasse(final String klasse) {

		this.klasse = klasse;
	}

	public String getStufe() {

		return stufe;
	}

	public void setStufe(final String stufe) {

		this.stufe = stufe;
	}

	public String getAusgabe() {

		return ausgabe;
	}

	public void setAusgabe(final String ausgabe) {

		this.ausgabe = ausgabe;
	}

	public String getJahr() {

		return jahr;
	}

	public void setJahr(final String jahr) {

		this.jahr = jahr;
	}

	public String getSeite() {

		return seite;
	}

	public void setSeite(final String seite) {

		this.seite = seite;
	}

	public String getPerson() {

		return person;
	}

	public void setPerson(final String person) {

		this.person = person;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public void setMediumUuid(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
	}

	public String getPfad() {

		return pfad;
	}

	public void setPfad(final String pfad) {

		this.pfad = pfad;
	}

}
