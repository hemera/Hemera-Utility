package hemera.utility.codec;

/**
 * <code>StringUtils</code> defines the implementation
 * of a unit that provides utility methods related to
 * string parsing operations.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum StringUtils {
	/**
	 * The singleton instance.
	 */
	instance;

	/**
	 * Parse the given string value into an array of
	 * letter and number only values.
	 * @param value The <code>String</code> to be parsed.
	 * @param separator The <code>String</code> separator
	 * used in between words.
	 * @return The <code>String</code> array of letter
	 * and number only values.
	 */
	public String[] parseArray(final String value, final String separator) {
		final String stripped = this.lettersAndNumbersOnly(value, separator);
		return stripped.split(separator);
	}
	
	/**
	 * Parse the given string by removing all non-
	 * letter or number characters.
	 * @param value The <code>String</code> value to
	 * be parsed.
	 * @param separator The <code>String</code> separator
	 * used in between words.
	 * @return The <code>String</code> containing
	 * only letters and numbers.
	 */
	public String lettersAndNumbersOnly(final String value, final String separator) {
		final int charcount = value.length();
		final StringBuilder builder = new StringBuilder();
		boolean appended = false;
		for (int i = 0; i < charcount; i++) {
			final char c = value.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				builder.append(c);
				appended = false;
			} else if (!appended) {
				builder.append(separator);
				appended = true;
			}
		}
		return builder.toString();
	}
}
