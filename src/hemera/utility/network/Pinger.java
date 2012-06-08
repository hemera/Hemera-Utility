package hemera.utility.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <code>Pinger</code> defines the implementation of
 * an utility unit that is responsible for pinging a
 * given host name and return its availability.
 * <p>
 * <code>Pinger</code> temporarily starts up a sub-
 * process to perform the pinging procedure. The sub-
 * process is terminated after the pinging.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public class Pinger {
	/**
	 * The ping count in <code>String</code> format.
	 */
	private final static String PingCount = "1";

	/**
	 * Ping the host with given host name.
	 * @param hostname The <code>String</code> host
	 * name to ping.
	 * @return <code>true</code> if the host with the
	 * given host name is available. <code>false</code>
	 * otherwise.
	 */
	public static boolean ping(final String hostname) {
		Process ping = null;
		try {
			ping = Runtime.getRuntime().exec(new String[]{"ping", "-c", PingCount, hostname});
			final BufferedReader reader = new BufferedReader(new InputStreamReader(ping.getInputStream()));
			String line = reader.readLine();
			if (line != null) return true;
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			ping.destroy();
		}
	}
}
