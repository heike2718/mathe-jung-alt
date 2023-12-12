// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.AbstractDomainEntity;

/**
 * Quelle
 */
@Schema(
	name = "Quelle",
	description = "Das Domain-Objekt Quelle. Die Attribute sind immer auf das assoziierte Medium bezogen. Aus diesen Attributen und dem Titel des Mediums werden je nach Quellenart Quellenangaben generiert. Der Name der Quelle wird durch das assoziierte Medium festgelegt.")
public class Quelle extends AbstractDomainEntity {

	@Schema(description = "Art der Quelle: Mensch, Buch, Zeitschrift")
	private Quellenart quellenart;

	@Schema(
		description = "Falls es sich z.B. um einen Wettbewerb handelte, ist dies der Text, der die Klasse beschreibt.")
	private String klasse;

	@Schema(
		description = "Falls es sich z.B. um einen Wettbewerb handelte, ist dies der Text, der die Stufe beschreibt.")
	private String stufe;

	@Schema(description = "Bei einer Zeitschrift die Nummer der Ausgabe.")
	private String ausgabe;

	@Schema(description = "Bei einer Zeitschrift, das Erscheinungsjahr, bei einem Wettbewerb das Wettbewerbsjahr.")
	private String jahr;

	@Schema(description = "Bei einem Buch oder einer Zeitschrift die Seite")
	private String seite;

	@Schema(description = "Wenn es eine bekannte Person ist, deren vollständiger Name")
	private String person;

	@Schema(description = "Wenn es eine Person mit Benutzerkonto ist, die UUID dieses Benutzerkontos")
	private String userId;

	@Schema(description = "Die Person, die das zugehörige Rätsel erfasst hat.")
	private String owner;

	@Schema(description = "Referenz auf ein Buch, eine Zeitschrift oder etwas im Internet, falls es keine Person ist")
	private String mediumUuid;

	/**
	 *
	 */
	protected Quelle() {

		super();

	}

	/**
	 * @param uuid
	 */
	public Quelle(final String uuid) {

		super(uuid);

	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof Quelle)) {

			return false;
		}

		return super.equals(obj);
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public Quelle withQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
		return this;
	}

	public String getKlasse() {

		return klasse;
	}

	public Quelle withKlasse(final String klasse) {

		this.klasse = klasse;
		return this;
	}

	public String getStufe() {

		return stufe;
	}

	public Quelle withStufe(final String stufe) {

		this.stufe = stufe;
		return this;
	}

	public String getAusgabe() {

		return ausgabe;
	}

	public Quelle withAusgabe(final String ausgabe) {

		this.ausgabe = ausgabe;
		return this;
	}

	public String getJahr() {

		return jahr;
	}

	public Quelle withJahr(final String jahr) {

		this.jahr = jahr;
		return this;
	}

	public String getSeite() {

		return seite;
	}

	public Quelle withSeite(final String seite) {

		this.seite = seite;
		return this;
	}

	public String getPerson() {

		return person;
	}

	public Quelle withPerson(final String person) {

		this.person = person;
		return this;
	}

	public String getUserId() {

		return userId;
	}

	public Quelle withUserId(final String userId) {

		this.userId = userId;
		return this;
	}

	public String getOwner() {

		return owner;
	}

	public Quelle withOwner(final String owner) {

		this.owner = owner;
		return this;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public Quelle withMediumUuid(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
		return this;
	}
}
