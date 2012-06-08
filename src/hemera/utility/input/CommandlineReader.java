package hemera.utility.input;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import hemera.core.utility.config.TimeData;

/**
 * <code>CommandlineReader</code> defines the utility
 * unit implementation that provides console command-
 * line reading functionalities.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public final class CommandlineReader {
	/**
	 * The <code>Logger</code> instance.
	 */
	private final Logger logger;
	/**
	 * The <code>Scanner</code> used to read in user
	 * inputs form command-line console.
	 */
	private final Scanner scanner;
	
	/**
	 * Constructor of <code>CommandlineReader</code>.
	 */
	public CommandlineReader() {
		this.logger = Logger.getLogger(CommandlineReader.class.getName());
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as a time data value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @return The <code>TimeData</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public TimeData promptReadTime(final String message, final boolean required) {
		while (true) {
			this.logger.info(message + "\nPlease enter time in the format of <value> <unit>. Permitted units: \"s\" \"ms\" \"ns\".");
			final String input = this.scanner.nextLine().trim();
			if (input == null || input.length() <= 0) {
				if (!required) return null;
				else this.logger.warning("Input cannot be empty.");
			} else {
				final String[] strs = input.split(" ");
				if (strs.length < 2) {
					this.logger.warning("Invalid input format.\nPlease enter time in the format of <value> <unit>. Permitted units: \"s\" \"ms\" \"ns\".");
					continue;
				}
				final String timevalueStr = strs[0];
				long timevalue = -1;
				try {
					timevalue = Long.valueOf(timevalueStr);
				} catch (NumberFormatException e) {
					if (!required) return null;
					else  {
						this.logger.warning("Invalid time value. Given time value must be a \"long\" value.");
						continue;
					}
				}
				final String timeunitStr = strs[1];
				TimeUnit timeunit = null;
				if (timeunitStr.equals("s")) timeunit = TimeUnit.SECONDS;
				else if (timeunitStr.equals("ms")) timeunit = TimeUnit.MILLISECONDS;
				else if (timeunitStr.equals("ns")) timeunit = TimeUnit.NANOSECONDS;
				else {
					this.logger.warning("Unsupported time unit: " + timeunitStr);
					continue;
				}
				return new TimeData(timevalue, timeunit);
			}
		}
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as a string value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @return The <code>String</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public String promptReadString(final String message, final boolean required) {
		String value = null;
		while (value == null) {
			this.logger.info(message);
			value = this.scanner.nextLine().trim();
			if (value == null || value.length() <= 0) {
				if (!required) return null;
				else this.logger.warning("Input cannot be empty.");
			}
		}
		return value;
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as a long value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @param min The <code>long</code> minimum value
	 * used to clamp the returned value.
	 * @param max The <code>long</code> maximum value
	 * used to clamp the returned value.
	 * @return The <code>Long</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public Long promptReadLong(final String message, final boolean required, final long min, final long max) {
		Long value = null;
		while (value == null) {
			this.logger.info(message);
			final String str = this.scanner.nextLine().trim();
			if (str == null || str.length() <= 0) {
				if (!required) return null;
				else this.logger.warning("Input cannot be empty.");
			} else {
				try {
					value = Long.valueOf(str);
				} catch (NumberFormatException e) {
					if (!required) return null;
					else  {
						this.logger.warning("Invalid value. Given value must be a \"long\" value.");
						continue;
					}
				}
			}
		}
		// Clamp.
		if (value < min) {
			value = min;
			this.logger.warning("Given value less than minimum value. Value set to minimum: " + min);
		}
		if (value > max) {
			value = max;
			this.logger.warning("Given value greater than maximum value. Value set to maximum: " + max);
		}
		return value;
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as an integer value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @param min The <code>int</code> minimum value
	 * used to clamp the returned value.
	 * @param max The <code>int</code> maximum value
	 * used to clamp the returned value.
	 * @return The <code>Integer</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public Integer promptReadInteger(final String message, final boolean required, final int min, final int max) {
		Integer value = null;
		while (value == null) {
			this.logger.info(message);
			final String str = this.scanner.nextLine().trim();
			if (str == null || str.length() <= 0) {
				if (!required) return null;
				else this.logger.warning("Input cannot be empty.");
			} else {
				try {
					value = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					if (!required) return null;
					else  {
						this.logger.warning("Invalid value. Given value must be a \"int\" value.");
						continue;
					}
				}
			}
		}
		// Clamp.
		if (value < min) {
			value = min;
			this.logger.warning("Given value less than minimum value. Value set to minimum: " + min);
		}
		if (value > max) {
			value = max;
			this.logger.warning("Given value greater than maximum value. Value set to maximum: " + max);
		}
		return value;
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as a double value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @param min The <code>double</code> minimum value
	 * used to clamp the returned value.
	 * @param max The <code>double</code> maximum value
	 * used to clamp the returned value.
	 * @return The <code>Double</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public Double promptReadDouble(final String message, final boolean required, final double min, final double max) {
		Double value = null;
		while (value == null) {
			this.logger.info(message);
			final String str = this.scanner.nextLine().trim();
			if (str == null || str.length() <= 0) {
				if (!required) return null;
				else this.logger.warning("Input cannot be empty.");
			} else {
				try {
					value = Double.valueOf(str);
				} catch (NumberFormatException e) {
					if (!required) return null;
					else  {
						this.logger.warning("Invalid value. Given value must be a \"double\" value.");
						continue;
					}
				}
			}
		}
		// Clamp.
		if (value < min) {
			value = min;
			this.logger.warning("Given value less than minimum value. Value set to minimum: " + min);
		}
		if (value > max) {
			value = max;
			this.logger.warning("Given value greater than maximum value. Value set to maximum: " + max);
		}
		return value;
	}
	
	/**
	 * Prompt the user with given message and read in
	 * the user input as a boolean value.
	 * @param message The <code>String</code> message
	 * to prompt to the user.
	 * @param required <code>true</code> if the user
	 * must enter a valid input as this value is a
	 * required field. <code>false</code> otherwise.
	 * @return The <code>Boolean</code> user entered
	 * input. <code>null</code> if this read is not
	 * for a required value and user chose to not to
	 * enter a value.
	 */
	public Boolean promptReadBoolean(final String message, final boolean required) {
		final StringBuilder builder = new StringBuilder();
		builder.append(message).append("\n");
		builder.append("Please enter boolean value in either the form of \"true\" or \"false\".");
		final String boolMessage = builder.toString();
		while (true) {
			final String input = this.promptReadString(boolMessage, required);
			if (input == null && !required) return null;
			else if (input.equals("true")) return true;
			else if (input.equals("false")) return false;
			else {
				this.logger.warning("Invalid boolean input.");
				continue;
			}
		}
	}
}
