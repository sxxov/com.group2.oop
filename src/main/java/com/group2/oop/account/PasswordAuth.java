package com.group2.oop.account;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hash passwords for storage, and test passwords against password tokens.
 *
 * Instances of this class can be used concurrently by multiple threads.
 *
 * @author erickson
 * @see <a href="http://stackoverflow.com/a/2861125/3474">StackOverflow</a>
 */
public final class PasswordAuth {

	/**
	 * Each token produced by this class uses this identifier as a prefix.
	 */
	public static final String ID = "$31$";

	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

	private static final int SIZE = 128;

	private static final Pattern layout = Pattern.compile(
		"\\$31\\$(\\d\\d?)\\$(.{43})"
	);

	private final SecureRandom random = new SecureRandom();

	private final int cost;

	public PasswordAuth() {
		this(16);
	}

	/**
	 * Create a password manager with a specified cost
	 *
	 * @param cost the exponential computational cost of hashing a password, 0 to 30
	 */
	public PasswordAuth(int cost) {
		assertValidCost(cost);
		this.cost = cost;
	}

	private static void assertValidCost(int cost) {
		if ((cost < 0) || (cost > 30)) throw new IllegalArgumentException(
			"Cost cannot be less than 0 or greater than 30"
		);
	}

	private static int getIterationCount(int cost) {
		return 1 << cost;
	}

	/**
	 * Hash a password for storage.
	 *
	 * @return a secure authentication token to be stored for later authentication
	 */
	public String hash(char[] password) {
		var salt = new byte[SIZE / 8];
		random.nextBytes(salt);

		var dk = pbkdf2(password, salt, getIterationCount(cost));
		var hash = new byte[salt.length + dk.length];
		System.arraycopy(salt, 0, hash, 0, salt.length);
		System.arraycopy(dk, 0, hash, salt.length, dk.length);

		var enc = Base64.getUrlEncoder().withoutPadding();

		return ID + cost + '$' + enc.encodeToString(hash);
	}

	/**
	 * Authenticate with a password and a stored password token.
	 *
	 * @return true if the password and token match
	 */
	public boolean authenticate(char[] password, String token) {
		var m = layout.matcher(token);
		if (!m.matches()) throw new IllegalArgumentException(
			"Invalid token format"
		);

		var cost = Integer.parseInt(m.group(1));
		assertValidCost(cost);
		var iterations = getIterationCount(cost);
		var hash = Base64.getUrlDecoder().decode(m.group(2));
		var salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
		var check = pbkdf2(password, salt, iterations);
		var zero = 0;

		for (var i = 0; i < check.length; ++i) {
			zero |= hash[salt.length + i] ^ check[i];
		}

		return zero == 0;
	}

	private static byte[] pbkdf2(
		char[] password,
		byte[] salt,
		int iterationCount
	) {
		var spec = new PBEKeySpec(password, salt, iterationCount, SIZE);

		try {
			var secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			return secretKeyFactory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
				"Missing algorithm: " + ALGORITHM,
				e
			);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException("Invalid SecretKeyFactory", e);
		}
	}
}
