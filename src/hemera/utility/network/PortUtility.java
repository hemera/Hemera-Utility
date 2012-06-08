package hemera.utility.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * <code>PortUtility</code> defines implementation of
 * a singleton utility unit that is responsible for
 * providing network port related functionalities. It
 * provides complete thread-safety guarantees with a
 * high level of concurrency capabilities.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum PortUtility {
	/**
	 * The <code>PortUtility</code> singleton instance.
	 */
	instance;

	/**
	 * Check if the given port number is available on
	 * the local host machine.
	 * @param port The <code>int</code> port number to
	 * be tested for availability.
	 * @return <code>true</code> if the given port is
	 * available on this machine. <code>false</code>
	 * otherwise.
	 */
	public boolean isAvailable(final int port) {
		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if(ss != null) {
				// Exception should not occur.
				try {ss.close();} catch (IOException e) {}
			}
			if(ds != null) {
				ds.close();
			}
		}
	}
}
