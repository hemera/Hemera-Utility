package hemera.utility.codec;

/**
 * <code>UnicodeUtils</code> defines a singleton unit
 * that provides utility methods related to unicode
 * parsing.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum UnicodeUtils {
	/**
	 * The singleton instance.
	 */
	instance;

	/**
	 * Convert the characters in given string value to
	 * ASCII characters if necessary. This method will
	 * convert single byte unicode characters that are
	 * outside the ASCII range into string values using
	 * ASCII characters. For instance, the emoticon
	 * character '\ue106', heart face, will be converted
	 * to the ASCII string of "\ue106".
	 * @param value The unicode based <code>String</code>
	 * value.
	 * @return The ASCII <code>String</code> value.
	 */
	public String toASCII(final String value) {
		final StringBuilder builder = new StringBuilder();
		final int length = value.length();
		for(int i = 0; i < length; i++) {
			final char c = value.charAt(i);
			// ASCII characters, does not need conversion.
			if ((c >= 0x0000) && (c <= 0x007F)) {
				builder.append(c);
			} else {
				// Standard unicode format.
				builder.append("\\u");
				// Get hex value of the character.
				final String hex = Integer.toHexString(c & 0xFFFF);
				// Prepend zeros because unicode requires 4 digits.
				final int hexlength = 4 - hex.length();
				for (int j = 0; j < hexlength; j++) {
					builder.append("0");
				}
				// Standard unicode format.
				builder.append(hex.toLowerCase());
			}
		}
		return builder.toString();
	}

	/**
	 * Convert the given ASCII string value to a unicode
	 * based string. For instance, the string "\ue106"
	 * will be converted into the emoticon single byte
	 * character '\ue106', heart face.
	 * @param value The <code>String</code> ASCII value.
	 * @return The unicode based <code>String</code> value.
	 */
	public String toUnicode(final String value) {
		final StringBuilder builder = new StringBuilder();
		final int length = value.length();
		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			// If character is '\\', check the following character.
			if (c == '\\' && i+1 < length) {
				c = value.charAt(i+1);
				i++;
				// If '\\' follows 'u', then the next four
				// characters form the hexadecimal of the
				// unicode single byte character.
				if (c == 'u') {
					i++;
					final String unicode = value.substring(i, i+4);
					// 16 radix hexadecimal.
					c = (char)Integer.parseInt(unicode, 16);
					i += 3;
				}
			}
			builder.append(c);
		}
		return builder.toString();
	}
}
