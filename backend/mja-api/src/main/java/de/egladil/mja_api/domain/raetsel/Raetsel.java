// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.dto.EmbeddableImageInfo;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.semantik.AggregateRoot;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import jakarta.validation.constraints.Pattern;

/**
 * Raetsel
 */
@AggregateRoot
@Schema(name = "Raetsel", description = "Die Details eines Rätsels - der Einfachheit halber mit Quellenangabe.")
public class Raetsel extends AbstractDomainEntity {

	@JsonProperty
	@Schema(description = "fachlicher Schlüssel im Aufgabenarchiv.")
	@Pattern(regexp = MjaRegexps.VALID_SCHLUESSEL, message = "schluessel muss aus genau 5 Ziffern bestehen")
	private String schluessel;

	@JsonProperty
	@Schema(description = "kurzer Titel zum Anzeigen in Suchergebnissen, volltextsuchfähig")
	private String name;

	@JsonProperty
	@Schema(description = "LaTeX-Code der Frage, volltextsuchfähig")
	private String frage;

	@JsonProperty
	@Schema(description = "LaTeX-Code der Lösung, volltextsuchfähig")
	private String loesung;

	@JsonProperty
	@Schema(description = "Kommentar, volltextsuchfähig")
	private String kommentar;

	@JsonProperty
	@Schema(description = "ob das Rätsel freigegeben ist.")
	private boolean freigegeben;

	@JsonProperty
	@Schema(description = "ob der Frage-Text die eventuell vorhandenen Antwortvorschläge bereits enthält.")
	private boolean antwortvorschlaegeEingebettet;

	@JsonIgnore
	private String filenameVorschauFrage;

	@JsonIgnore
	private String filenameVorschauLoesung;

	@Schema(description = "Der Herkunftstyp: EIGENKREATION, ZITAT, ADAPTION")
	private RaetselHerkunftTyp herkunftstyp;

	@JsonProperty
	@Schema(
		description = "Daten einer Quelle für ein Raetsel. Nicht alle Attribute zusammen sind sinnvoll. Eingie schließen einander aus.")
	private QuelleDto quelle;

	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe", example = "alpha (6) 1976, S.32")
	private String quellenangabe;

	@JsonProperty
	@Schema(description = "Zeigt an, ob die Person, die das Rätsel geladen hat, änderungsberechtigt ist.")
	private boolean schreibgeschuetzt = true; // erstmal immer schreibgeschuetzt. Beim Laden der Details wird entschieden, ob es
												// durch den User änderbar ist.

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = Antwortvorschlag.class,
		description = "optionale Antwortvorschläge, wenn es für multiple choice genutzt werden kann")
	private Antwortvorschlag[] antwortvorschlaege;

	@JsonProperty
	@Schema(type = SchemaType.ARRAY, implementation = Deskriptor.class, description = "Deskriptoren, für das Rätsel")
	private List<Deskriptor> deskriptoren;

	@JsonProperty
	@Schema(
		type = SchemaType.ARRAY, implementation = EmbeddableImageInfo.class,
		description = "Info über die im LaTeX-Code der Frage oder Lösung eingebundenen eps-Files")
	private List<EmbeddableImageInfo> embeddableImageInfos = new ArrayList<>();

	@Schema(description = "Images, die angezeigt werden können")
	@JsonProperty
	private Images images;

	@JsonProperty
	@Schema(description = "PDF")
	private byte[] raetselPDF;

	/**
	 *
	 */
	protected Raetsel() {

		super();

	}

	/**
	 * @param uuid
	 */
	public Raetsel(final String uuid) {

		super(uuid);

	}

	public String antwortvorschlaegeAsJSON() {

		return AntwortvorschlaegeMapper.antwortvorschlaegeAsJSON(antwortvorschlaege);
	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof Raetsel)) {

			return false;
		}

		return super.equals(obj);
	}

	public String getSchluessel() {

		return schluessel;
	}

	public void setSchluessel(final String schluessel) {

		this.schluessel = schluessel;
	}

	public Raetsel withSchluessel(final String schluessel) {

		this.schluessel = schluessel;
		return this;
	}

	public String getName() {

		return name;
	}

	public Raetsel withName(final String name) {

		this.name = name;
		return this;
	}

	public String getFrage() {

		return frage;
	}

	public Raetsel withFrage(final String frage) {

		this.frage = frage;
		return this;
	}

	public String getLoesung() {

		return loesung;
	}

	public Raetsel withLoesung(final String loesung) {

		this.loesung = loesung;
		return this;
	}

	public String getKommentar() {

		return kommentar;
	}

	public Raetsel withKommentar(final String kommentar) {

		this.kommentar = kommentar;
		return this;
	}

	public Antwortvorschlag[] getAntwortvorschlaege() {

		return antwortvorschlaege;
	}

	public Raetsel withAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		this.antwortvorschlaege = antwortvorschlaege;
		return this;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public Raetsel withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

	public Images getImages() {

		return images;
	}

	public void setImages(final Images images) {

		this.images = images;
	}

	public List<EmbeddableImageInfo> getEmbeddableImageInfos() {

		return embeddableImageInfos;
	}

	public void addAllEmbeddableImageInfos(final List<EmbeddableImageInfo> embeddableImageInfos) {

		this.embeddableImageInfos.addAll(embeddableImageInfos);
	}

	public void setSchreibgeschuetzt(final boolean schreibgeschuetzt) {

		this.schreibgeschuetzt = schreibgeschuetzt;
	}

	public boolean isSchreibgeschuetzt() {

		return schreibgeschuetzt;
	}

	public String getFilenameVorschauFrage() {

		return filenameVorschauFrage;
	}

	public Raetsel withFilenameVorschauFrage(final String filenameVorschauFrage) {

		this.filenameVorschauFrage = filenameVorschauFrage;
		return this;
	}

	public String getFilenameVorschauLoesung() {

		return filenameVorschauLoesung;
	}

	public Raetsel withFilenameVorschauLoesung(final String filenameVorschauLoesung) {

		this.filenameVorschauLoesung = filenameVorschauLoesung;
		return this;
	}

	public boolean isFreigegeben() {

		return freigegeben;
	}

	public Raetsel withFreigegeben(final boolean freigegeben) {

		this.freigegeben = freigegeben;
		return this;
	}

	public QuelleDto getQuelle() {

		return quelle;
	}

	public void setQuelle(final QuelleDto quelle) {

		this.quelle = quelle;
	}

	public Raetsel withQuelle(final QuelleDto quelle) {

		this.quelle = quelle;
		return this;
	}

	public String getQuellenangabe() {

		return quellenangabe;
	}

	public void setQuellenangabe(final String quellenangabe) {

		this.quellenangabe = quellenangabe;
	}

	public Raetsel withQuellenangabe(final String quellenangabe) {

		this.quellenangabe = quellenangabe;
		return this;
	}

	public RaetselHerkunftTyp getHerkunftstyp() {

		return herkunftstyp;
	}

	public Raetsel withHerkunftstyp(final RaetselHerkunftTyp herkunftstyp) {

		this.herkunftstyp = herkunftstyp;
		return this;
	}

	public boolean isAntwortvorschlaegeEingebettet() {

		return antwortvorschlaegeEingebettet;
	}

	public Raetsel withAntwortvorschlaegeEingebettet(final boolean antwortvorschlaegeEingebettet) {

		this.antwortvorschlaegeEingebettet = antwortvorschlaegeEingebettet;
		return this;
	}
}
