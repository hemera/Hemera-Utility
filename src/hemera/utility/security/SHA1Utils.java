package hemera.utility.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * <code>SHA1Utils</code> defines the singleton utility
 * unit that provides SHA1 algorithm based encryption.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum SHA1Utils {
	/**
	 * The singleton instance.
	 */
	instance;

	/**
	 * Encrypt the given string value using SHA-1 one
	 * way hashing and return the hexadecimal encoded
	 * string.
	 * @param original The <code>String</code> value
	 * to be encrypted.
	 * @param iteration The <code>int</code> number of
	 * times the SHA1 encryption is performed.
	 * @return The encrypted and hexadecimal encoded
	 * <code>String</code>.
	 * @throws NoSuchAlgorithmException If the SHA1
	 * algorithm is not supported.
	 * @throws UnsupportedEncodingException If UTF-8
	 * encoding is not supported.
	 */
	public String encrypt(final String original, final int iteration) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] bytes = original.getBytes("UTF-8");
		for (int i = 0; i < iteration; i++) {
			bytes = digest.digest(bytes);
		}
		final char[] chars = Hex.encodeHex(bytes);
		final String encrypted = new String(chars);
		return encrypted;
	}
}
