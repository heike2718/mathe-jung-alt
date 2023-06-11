// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.security.runtime.QuarkusPrincipal;

/**
 * AuthenticatedUser
 */
public class AuthenticatedUser extends QuarkusPrincipal {

	@JsonProperty
	private String idReference;

	@JsonProperty
	private String[] roles;

	@JsonProperty
	private String fullName;

	/**
	 * @param name
	 */
	public AuthenticatedUser(final String name) {

		super(name);

	}

	/**
	 * @param idReference
	 * @param rolle
	 * @param fullName
	 * @param uuid
	 */
	public static AuthenticatedUser create(final String idReference, final String[] roles, final String fullName, final String uuid) {

		AuthenticatedUser result = new AuthenticatedUser(uuid);
		result.idReference = idReference;
		result.roles = roles;
		result.fullName = fullName;
		return result;
	}

	@Override
	public String toString() {

		return "AuthenticatedUser [uuid=" + StringUtils.abbreviate(getName(), 11) + ", roles=" + Arrays.toString(roles) + "]";
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

		return getName();
	}

	/**
	 * @return the roles
	 */
	public String[] getRoles() {

		return roles;
	}

	public AuthenticatedUser withIdReference(final String idReference) {

		this.idReference = idReference;
		return this;
	}

	public AuthenticatedUser withRoles(final String[] roles) {

		this.roles = roles;
		return this;
	}

	public AuthenticatedUser withFullName(final String fullName) {

		this.fullName = fullName;
		return this;
	}
}
