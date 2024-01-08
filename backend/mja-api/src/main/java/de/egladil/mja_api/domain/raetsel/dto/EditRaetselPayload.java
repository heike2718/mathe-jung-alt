// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * EditRaetselPayload
 */
@Schema(name = "EditRaetselPayload", description = "Payload zum Anlegen und Ändern eines Rätsels")
public class EditRaetselPayload {

	@Schema(description = "technische ID, 'neu' für neue Rätsel", example = "0870a50a-bef4-467f-bae2-f94761aa5f3b")
	@Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "die id enthält ungültige Zeichen")
	private String id;

	@Schema(
		description = "Flag, ob Änderungen im LaTeX-Code historisiert werden sollen. Ist nur bei inhaltlichen Änderungen sinnvoll.")
	private boolean latexHistorisieren;

	@JsonProperty
	@Schema(description = "fachlicher Schlüssel im Aufgabenarchiv.", example = "05463")
	@Pattern(regexp = MjaRegexps.VALID_EDIT_PAYLOOAD_SCHLUESSEL, message = "falls schluessel, dann nur Ziffern")
	@Size(max = 5, message = "schluessel darf nicht aus mehr als 5 Ziffern bestehen")
	private String schluessel;

	@JsonProperty
	@Schema(
		description = "kurzer Titel zum Anzeigen in Suchergebnissen, volltextsuchfähig", required = true,
		example = "Subtraktion")
	private String name;

	@JsonProperty
	@Schema(description = "LaTeX-Code der Frage, volltextsuchfähig", required = true, example = "[\\1234-987=\\]")
	private String frage;

	@JsonProperty
	@Schema(
		description = "LaTeX-Code der Lösung, volltextsuchfähig",
		example = "237")
	private String loesung;

	@JsonProperty
	@Schema(description = "Kommentar, volltextsuchfähig", example = "Serie Serie-1, 1, Serie-65, 65")
	private String kommentar;

	@JsonProperty
	@Schema(description = "ob das Rätsel freigegeben ist.")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "ob der Frage-Text die eventuell vorhandenen Antwortvorschläge bereits enthält.")
	private boolean antwortvorschlaegeEingebettet;

	@JsonProperty
	@Schema(description = "Herkunftstyp des Rätsels", required = true)
	private RaetselHerkunftTyp herkunftstyp;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = Antwortvorschlag.class,
		description = "optionale Antwortvorschläge, wenn es für multiple choice genutzt werden kann")
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	@Schema(type = SchemaType.ARRAY, implementation = Deskriptor.class, description = "Deskriptoren, für das Rätsel")
	private List<Deskriptor> deskriptoren;

	@Schema(description = "Die Quelle für dieses Rätsel", required = true)
	private QuelleDto quelle;

	public String getId() {

		return id;
	}

	public EditRaetselPayload withId(final String id) {

		this.id = id;
		return this;
	}

	public boolean isLatexHistorisieren() {

		return latexHistorisieren;
	}

	public EditRaetselPayload withLatexHistorisieren(final boolean latexHistorisieren) {

		this.latexHistorisieren = latexHistorisieren;
		return this;
	}

	public QuelleDto getQuelle() {

		return quelle;
	}

	public EditRaetselPayload withQuelle(final QuelleDto quelle) {

		this.quelle = quelle;
		return this;
	}

	public void setSchluessel(final String schluessel) {

		this.schluessel = schluessel;
	}

	public String getSchluessel() {

		return schluessel;
	}

	public EditRaetselPayload withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public String getName() {

		return name;
	}

	public EditRaetselPayload withName(final String name) {

		this.name = name;
		return this;
	}

	public String getFrage() {

		return frage;
	}

	public EditRaetselPayload withFrage(final String frage) {

		this.frage = frage;
		return this;
	}

	public String getLoesung() {

		return loesung;
	}

	public EditRaetselPayload withLoesung(final String loesung) {

		this.loesung = loesung;
		return this;
	}

	public String getKommentar() {

		return kommentar;
	}

	public EditRaetselPayload withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public EditRaetselPayload withFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
		return this;
	}

	public RaetselHerkunftTyp getHerkunftstyp() {

		return herkunftstyp;
	}

	public EditRaetselPayload withHerkunftstyp(final RaetselHerkunftTyp herkunftstyp) {

		this.herkunftstyp = herkunftstyp;
		return this;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public EditRaetselPayload withAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
		return this;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public EditRaetselPayload withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

	public boolean isAntwortvorschlaegeEingebettet() {

		return antwortvorschlaegeEingebettet;
	}

	public EditRaetselPayload withAntwortvorschlaegeEingebettet(final boolean antwortvorschlaegeEingebettet) {

		this.antwortvorschlaegeEingebettet = antwortvorschlaegeEingebettet;
		return this;
	}
}
