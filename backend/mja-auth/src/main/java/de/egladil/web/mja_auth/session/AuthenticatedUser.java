// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.session;

import java.security.Principal;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthenticatedUser
 */
public class AuthenticatedUser implements Principal {

	@JsonProperty
	private final String idReference;

	@JsonProperty
	private final String[] roles;

	@JsonProperty
	private final String fullName;

	@JsonIgnore
	private final String uuid;

	/**
	 * @param idReference
	 * @param rolle
	 * @param fullName
	 * @param uuid
	 */
	public AuthenticatedUser(final String idReference, final String[] roles, final String fullName, final String uuid) {

		this.idReference = idReference;
		this.roles = roles;
		this.fullName = fullName;
		this.uuid = uuid;
	}

	@Override
	public String toString() {

		return "AuthenticatedUser [fullName=" + fullName + ", roles=" + Arrays.toString(roles) + ", uuid=" + uuid + "]";
	}

	@Override
	@JsonIgnore
	public String getName() {

		return this.uuid;
	}

	/**
	 * @return the idReference
	 */
	public String getIdReference() {

		return idReference;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {

		return fullName;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {

		return uuid;
	}

	/**
	 * @return the roles
	 */
	public String[] getRoles() {

		return roles;
	}

}
