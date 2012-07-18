package hemera.utility.security;

import java.util.HashMap;

import junit.framework.TestCase;

public class TestAES extends TestCase {

	public void test() throws Exception {
		final int count = 100000;
		final HashMap<String, String> map = new HashMap<String, String>(count);

		final String seed = "FD0B5FE7357c47ca";
		for (int i = 0; i < count; i++) {
			// Create a key and passcode.
			final String rawKey = AESUtils.instance.generateKey(seed, 128);
			final String passcode = AESUtils.instance.generateKey(seed, 128);
			System.out.println("Raw key:         " + rawKey);
			System.out.println("Passcode:        " + passcode);
			// Store the key.
			if (map.containsKey(rawKey)) fail("Generate a duplicate key.");
			map.put(rawKey, rawKey);
			// Encrypt the key with passcode.
			final String encryptedKey = AESUtils.instance.encrypt(rawKey, passcode);
			System.out.println("Encrypted key:   " + encryptedKey);
			// Decrypt the encrypted with passcode.
			final String decryptedKey = AESUtils.instance.decrypt(encryptedKey, passcode);
			System.out.println("Decrypted key:   " + decryptedKey);
			// Test.
			final boolean succeeded = rawKey.equals(decryptedKey);
			if (!succeeded) fail("Decrypted key is not the same as the raw key.");
			System.out.println("Success: " + succeeded);
			System.out.println("=================================================");
		}
	}
}
