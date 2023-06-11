// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;

/**
 * SecureTokenService
 */
@ApplicationScoped
public class SecureTokenService {

	private static final byte TOKEN_BYTES = 16;

	private static final byte SIGNATURE_BYTES = 32; // Length of SHA-256

	private final SecureRandom secureRandom;

	private final byte[] secret = new byte[TOKEN_BYTES];

	/**
	 *
	 */
	public SecureTokenService() throws NoSuchAlgorithmException {

		super();
		secureRandom = SecureRandom.getInstanceStrong();
		secureRandom.nextBytes(secret);

	}

	public String createRandomToken() {

		try {

			byte[] tokenAndSignature = new byte[TOKEN_BYTES + SIGNATURE_BYTES];
			// Add random token
			secureRandom.nextBytes(tokenAndSignature);

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(tokenAndSignature, 0, TOKEN_BYTES);
			messageDigest.update(secret);
			// Add signature
			messageDigest.digest(tokenAndSignature, TOKEN_BYTES, SIGNATURE_BYTES);

			return Base64.getEncoder().encodeToString(tokenAndSignature);
		} catch (NoSuchAlgorithmException | DigestException e) {

			throw new IllegalStateException(e);
		}
	}

	public boolean validateToken(final String encodedTokenAndSignature) {

		try {

			byte[] tokenAndSignature = Base64.getDecoder().decode(encodedTokenAndSignature);

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			// Read only the plain token part
			messageDigest.update(tokenAndSignature, 0, TOKEN_BYTES);
			messageDigest.update(secret);
			byte[] expectedSignature = messageDigest.digest();

			// Compare actual signature with the expected one
			return Arrays.equals(tokenAndSignature, TOKEN_BYTES, TOKEN_BYTES + SIGNATURE_BYTES, expectedSignature, 0,
				SIGNATURE_BYTES);
		} catch (NoSuchAlgorithmException e) {

			throw new IllegalStateException(e);
		}
	}
}
