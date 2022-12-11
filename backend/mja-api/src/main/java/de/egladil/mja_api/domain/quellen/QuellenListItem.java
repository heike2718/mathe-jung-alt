// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.AbstractDomainEntity;
import de.egladil.mja_api.domain.semantik.AggregateRoot;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;

/**
 * QuellenListItem
 */
@AggregateRoot
@Schema(name = "Quelle", description = "Quelle für ein Rätsel")
public class QuellenListItem extends AbstractDomainEntity {

	@Schema(description = "Art der Quelle: Mensch, Buch, Zeitschrift")
	private Quellenart quellenart;

	@Schema(description = "Zahl zum Sortieren")
	private long sortNumber;

	@Schema(description = "menschenlesbarer Anzeigetext für eine Quellenangabe")
	private String name;

	@Schema(description = "Referenz auf ein Buch oder eine Zeitschrift")
	private String mediumUuid;

	@Schema(type = SchemaType.ARRAY, implementation = Deskriptor.class, description = "Deskriptoren, für die Quelle")
	private List<Deskriptor> deskriptoren;

	/**
	 *
	 */
	protected QuellenListItem() {

	}

	/**
	 * @param id
	 */
	public QuellenListItem(final String uuid) {

		super(uuid);
	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof QuellenListItem)) {

			return false;
		}

		return super.equals(obj);
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public QuellenListItem withQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
		return this;
	}

	public long getSortNumber() {

		return sortNumber;
	}

	public QuellenListItem withSortNumber(final long sortNumber) {

		this.sortNumber = sortNumber;
		return this;
	}

	public String getName() {

		return name;
	}

	public QuellenListItem withName(final String name) {

		this.name = name;
		return this;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public QuellenListItem withMediumIdentifier(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
		return this;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public QuellenListItem withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

}
