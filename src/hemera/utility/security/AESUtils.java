package hemera.utility.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * <code>AESUtils</code> defines the singleton utility
 * unit that provides AES algorithm based encryption
 * and decryption.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum AESUtils {
	/**
	 * The singleton instance.
	 */
	instance;
	
	/**
	 * Encrypt the given string with the specified hex
	 * key using the AES algorithm.
	 * @param original The <code>String</code> value
	 * to be encrypted.
	 * @param hexKey The hex <code>String</code> key
	 * to encrypt the value with. You may use the
	 * <code>generateKey</code> method to create a
	 * hex key.
	 * @return The encrypted hex <code>String</code>.
	 * @throws NoSuchAlgorithmException If AES is not
	 * supported.
	 * @throws NoSuchPaddingException If transformation
	 * contains a padding that is not available.
	 * @throws InvalidKeyException If the given key is
	 * inappropriate for initializing this cipher, or
	 * if the given key has a size that exceeds the
	 * maximum allowable key size (as determined from
	 * the configured jurisdiction policy files).
	 * @throws IllegalBlockSizeException If the total
	 * input length of the data processed by this cipher
	 * is not a multiple of block size; or if the AES
	 * encryption algorithm is unable to process the
	 * input data provided.
	 * @throws BadPaddingException Should not occur.
	 * @throws UnsupportedEncodingException If UTF-8
	 * encoding is not supported.
	 * @throws DecoderException If decoding hex key
	 * failed.
	 */
	public String encrypt(final String original, final String hexKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
	InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, DecoderException {
		final byte[] keybytes = Hex.decodeHex(hexKey.toCharArray());
		final SecretKeySpec keySpec = new SecretKeySpec(keybytes, "AES");
		final Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		final byte[] encrypted = cipher.doFinal(original.getBytes("UTF-8"));
		final char[] chars = Hex.encodeHex(encrypted);
		return new String(chars);
	}

	/**
	 * Decrypt the given hex string value with the given
	 * hex key using AES algorithm.
	 * @param hex The hex <code>String</code> value to
	 * be decrypted. This value should be produced by
	 * the <code>encrypt</code> method.
	 * @param hexKey The hex <code>String</code> key
	 * to encrypt the value with.
	 * @return The decrypted <code>String</code> value.
	 * @throws NoSuchAlgorithmException If AES is not
	 * supported.
	 * @throws NoSuchPaddingException If transformation
	 * contains a padding that is not available.
	 * @throws InvalidKeyException If the given key is
	 * inappropriate for initializing this cipher, or
	 * if the given key has a size that exceeds the
	 * maximum allowable key size (as determined from
	 * the configured jurisdiction policy files).
	 * @throws IllegalBlockSizeException If the total
	 * input length of the data processed by this cipher
	 * is not a multiple of block size; or if the AES
	 * encryption algorithm is unable to process the
	 * input data provided.
	 * @throws BadPaddingException Should not occur.
	 * @throws UnsupportedEncodingException If UTF-8
	 * encoding is not supported.
	 * @throws DecoderException If decoding hex value
	 * failed.
	 */
	public String decrypt(final String hex, final String hexKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
	InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, DecoderException {
		final byte[] keybytes = Hex.decodeHex(hexKey.toCharArray());
		final SecretKeySpec keySpec = new SecretKeySpec(keybytes, "AES");
		final Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		final byte[] bytes = cipher.doFinal(Hex.decodeHex(hex.toCharArray()));
		return new String(bytes, "UTF-8");
	}
	
	/**
	 * Generate an encryption key using the given seed
	 * and with the specified bit count.
	 * @param seed The <code>String</code> seed to use.
	 * @param bitCount The <code>int</code> number of
	 * bits the key should have. This value must be
	 * either 128, 192 or 256.
	 * @return The <code>String</code> hex encoded key.
	 * @throws NoSuchAlgorithmException If AES algorithm
	 * is not supported.
	 * @throws UnsupportedEncodingException If UTF-8
	 * encoding is not supported.
	 */
	public String generateKey(final String seed, final int bitCount) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (bitCount != 128 && bitCount != 192 && bitCount != 256) {
			throw new IllegalArgumentException("Bit count must be either 128, 192 or 256.");
		}
		final KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(new SecureRandom(seed.getBytes("UTF-8")));
		generator.init(bitCount);
		final SecretKey key = generator.generateKey();
		final byte[] bytes = key.getEncoded();
		return Hex.encodeHexString(bytes);
	}
}
