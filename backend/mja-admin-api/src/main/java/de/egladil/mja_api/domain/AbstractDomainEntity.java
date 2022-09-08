// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AbstractDomainEntity
 */
public abstract class AbstractDomainEntity {

	public static final String UUID_NEUE_ENTITY = "neu";

	private String id;

	/**
	 *
	 */
	protected AbstractDomainEntity() {

		super();

	}

	/**
	 * @param id
	 */
	public AbstractDomainEntity(final String uuid) {

		this.id = uuid;
	}

	/**
	 * @return the id
	 */
	@JsonProperty
	public String getId() {

		return id;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (!(obj instanceof AbstractDomainEntity)) {

			return false;
		}
		AbstractDomainEntity other = (AbstractDomainEntity) obj;

		if (id == null) {

			if (other.id != null) {

				return false;
			}
		} else if (!id.equals(other.id)) {

			return false;
		}
		return true;
	}

	public void setId(final String id) {

		this.id = id;
	}

}
