// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * JWTService
 */
public interface JWTService {

	/**
	 * Dekodiert und verifiziert das jwt.
	 *
	 * @param  jwt
	 * @param  publicKeyData
	 * @return                          DecodedJWT
	 * @throws JWTVerificationException
	 */
	DecodedJWT verify(String jwt, byte[] publicKeyData) throws JWTVerificationException;

}
