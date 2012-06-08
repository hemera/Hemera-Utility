package hemera.utility.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import hemera.core.utility.logging.FileLogger;

/**
 * <code>SecurityUtils</code> defines the singleton
 * implementation that provides utility logic methods
 * to perform security related actions.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum SecurityUtils {
	/**
	 * The singleton instance.
	 */
	instance;
	
	/**
	 * The <code>FileLogger</code> instance.
	 */
	private final FileLogger logger;
	
	/**
	 * Constructor of <code>SecurityUtils</code>.
	 */
	private SecurityUtils() {
		this.logger = FileLogger.getLogger(this.getClass());
	}
	
	/**
	 * Encrypt the given string value using SHA-1 one
	 * way hashing and return the hexadecimal encoded
	 * string.
	 * @param original The <code>String</code> value
	 * to be encrypted.
	 * @param iteration The <code>int</code> number of
	 * times the SHA1 encryption is performed.
	 * @return The encrypted and hexadecimal encoded
	 * <code>String</code>. <code>null</code> if any
	 * error occurred, which will be logged.
	 */
	public String encryptSHA1(final String original, final int iteration) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = original.getBytes("UTF-8");
			for (int i = 0; i < iteration; i++) {
				bytes = digest.digest(bytes);
			}
			final char[] chars = Hex.encodeHex(bytes);
			final String encrypted = new String(chars);
			return encrypted;
		} catch (final NoSuchAlgorithmException e) {
			this.logger.severe("SHA-1 algorithm not available.");
			this.logger.exception(e);
		} catch (final UnsupportedEncodingException e) {
			this.logger.severe("UTF-8 encoding not supported.");
			this.logger.exception(e);
		}
		return null;
	}
}
