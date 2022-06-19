// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.jwt;

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
