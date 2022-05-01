// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain;

/**
 * AbstractDomainEntity
 */
public abstract class AbstractDomainEntity {

	private String uuid;

	/**
	 *
	 */
	protected AbstractDomainEntity() {

		super();

	}

	/**
	 * @param uuid
	 */
	public AbstractDomainEntity(final String uuid) {

		this.uuid = uuid;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {

		return uuid;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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

		if (uuid == null) {

			if (other.uuid != null) {

				return false;
			}
		} else if (!uuid.equals(other.uuid)) {

			return false;
		}
		return true;
	}

}
