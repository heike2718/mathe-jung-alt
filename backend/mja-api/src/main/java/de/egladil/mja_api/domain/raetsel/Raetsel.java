// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetsel.dto.GrafikInfo;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.semantik.AggregateRoot;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;

/**
 * Raetsel
 */
@AggregateRoot
@Schema(name = "Raetsel", description = "Stammdaten eines Rätsels")
public class Raetsel extends AbstractDomainEntity {

	@JsonProperty
	@Schema(description = "fachlicher Schlüssel im Aufgabenarchiv.")
	@Pattern(regexp = "^[\\d]{5}$", message = "schluessel muss aus genau 5 Ziffern bestehen")
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
	@Schema(description = "Veröffentlichungsstatus des Rätsels, nur freigegebene können in der mja-app gefunden werden.")
	private DomainEntityStatus status;

	@JsonProperty
	@Schema(description = "Referenz auf die Quelle des Rätsels")
	@Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "quelleID enthält ungültige Zeichen - muss eine UUID sein")
	private String quelleId;

	@JsonProperty
	@Schema(description = "Zeigt an, ob die Person, die das Rätsel geladen hat, änderungsberechtigt ist.")
	private boolean schreibgeschuetzt = true; // erstmal immer schreibgeschuetzt. Beim Laden der Details wird entschieden, ob es durch den User
										// änderbar ist.

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
		type = SchemaType.ARRAY, implementation = GrafikInfo.class,
		description = "Info über die im LaTeX-Code der Frage oder Lösung eingebundenen eps-Images.")
	private List<GrafikInfo> grafikInfos = new ArrayList<>();

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

	public String getQuelleId() {

		return quelleId;
	}

	public Raetsel withQuelleId(final String quelleId) {

		this.quelleId = quelleId;
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

	public DomainEntityStatus getStatus() {

		return status;
	}

	public Raetsel withStatus(final DomainEntityStatus status) {

		this.status = status;
		return this;
	}

	public List<GrafikInfo> getGrafikInfos() {

		return grafikInfos;
	}

	public void setGrafikInfos(final List<GrafikInfo> grafikInfos) {

		this.grafikInfos = grafikInfos;
	}

	public boolean isSchreibgeschuetzt() {

		return schreibgeschuetzt;
	}

	public void markiereAlsAenderbar() {

		this.schreibgeschuetzt = false;
	}
}
