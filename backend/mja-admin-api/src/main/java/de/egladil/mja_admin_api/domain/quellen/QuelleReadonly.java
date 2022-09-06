// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.quellen;

import java.util.List;

import de.egladil.mja_admin_api.domain.AbstractDomainEntity;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_shared.semantik.AggregateRoot;

/**
 * QuelleReadonly
 */
@AggregateRoot
public class QuelleReadonly extends AbstractDomainEntity {

	private Quellenart quellenart;

	private long sortNumber;

	private String name;

	private String mediumUuid;

	private List<Deskriptor> deskriptoren;

	/**
	 *
	 */
	protected QuelleReadonly() {

	}

	/**
	 * @param id
	 */
	public QuelleReadonly(final String uuid) {

		super(uuid);
	}

	@Override
	public int hashCode() {

		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (!(obj instanceof QuelleReadonly)) {

			return false;
		}

		return super.equals(obj);
	}

	public Quellenart getQuellenart() {

		return quellenart;
	}

	public QuelleReadonly withQuellenart(final Quellenart quellenart) {

		this.quellenart = quellenart;
		return this;
	}

	public long getSortNumber() {

		return sortNumber;
	}

	public QuelleReadonly withSortNumber(final long sortNumber) {

		this.sortNumber = sortNumber;
		return this;
	}

	public String getName() {

		return name;
	}

	public QuelleReadonly withName(final String name) {

		this.name = name;
		return this;
	}

	public String getMediumUuid() {

		return mediumUuid;
	}

	public QuelleReadonly withMediumIdentifier(final String mediumUuid) {

		this.mediumUuid = mediumUuid;
		return this;
	}

	public List<Deskriptor> getDeskriptoren() {

		return deskriptoren;
	}

	public QuelleReadonly withDeskriptoren(final List<Deskriptor> deskriptoren) {

		this.deskriptoren = deskriptoren;
		return this;
	}

}
