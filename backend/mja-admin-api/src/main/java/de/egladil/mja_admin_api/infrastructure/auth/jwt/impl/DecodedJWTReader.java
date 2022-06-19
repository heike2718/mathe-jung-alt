// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.jwt.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.jwt.Claims;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * DecodedJWTReader
 */
public class DecodedJWTReader {

	private final DecodedJWT decodedJWT;

	/**
	 * @param decodedJWT
	 */
	public DecodedJWTReader(final DecodedJWT decodedJWT) {

		this.decodedJWT = decodedJWT;
	}

	public String getFullName() {

		return decodedJWT.getClaim(Claims.full_name.name()).asString();
	}

	public Set<String> getGroups() {

		return new HashSet<>(decodedJWT.getClaim(Claims.groups.name()).asList(String.class));
	}

}
