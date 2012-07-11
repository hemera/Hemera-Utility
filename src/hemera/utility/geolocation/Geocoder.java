package hemera.utility.geolocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <code>UGeocoder</code> defines the singleton utility
 * unit responsible for geocoding a given address into
 * latitude and longitude coordinates.
 *
 * @author Yi Wang (Neakor)
 * @version 1.0.0
 */
public enum Geocoder {
	/**
	 * The singleton instance.
	 */
	instance;
	
	/**
	 * The <code>String</code> Google API URL.
	 */
	private final String googleURL = "http://maps.google.com/maps/api/geocode/xml?sensor=false&address=";
	/**
	 * The <code>String</code> location tag name.
	 */
	private final String locationTag = "location";
	/**
	 * The <code>String</code> latitude tag name.
	 */
	private final String latitudeTag = "lat";
	/**
	 * The <code>String</code> longitude tag name.
	 */
	private final String longitudeTag = "lng";

	/**
	 * Geocode the given address.
	 * @param street The <code>String</code> street.
	 * This value may be <code>null</code>.
	 * @param city The <code>String</code> city.
	 * @param state The <code>String</code> state.
	 * @return The <code>Coordinates</code> result.
	 * <code>null</code> if the address is invalid.
	 * @throws IOException If geocoding failed.
	 */
	public Coordinates geocode(final String street, final String city, final String state) throws IOException {
		final String address = this.buildAddress(street, city, state);
		final String urlstr = this.googleURL + address;
		final URL url = new URL(urlstr);
		final String response = this.buildResponse(url);
		return this.parseCoordinates(response);
	}
	
	/**
	 * Build the address string using the given values.
	 * @param street The <code>String</code> street.
	 * @param city The <code>String</code> city.
	 * @param state The <code>String</code> state.
	 * @return The <code>String</code> address in the
	 * format of [street,city,state].
	 * @throws UnsupportedEncodingException If UTF-8
	 * encoding is not supported.
	 */
	private String buildAddress(final String street, final String city, final String state) throws UnsupportedEncodingException {
		final StringBuilder builder = new StringBuilder();
		if (street != null && street.length() > 0) {
			builder.append(street).append(",");
		}
		builder.append(city).append(",").append(state);
		final String raw = builder.toString();
		return URLEncoder.encode(raw, "UTF-8");
	}
	
	/**
	 * Construct the response XML string using the
	 * given URL response.
	 * @param url The <code>URL</code> to receive the
	 * response from.
	 * @return The <code>String</code> XML response.
	 * @throws IOException If receiving response failed.
	 */
	private String buildResponse(final URL url) throws IOException {
		// Construct response.
		final InputStream stream = url.openStream();
		final StringBuilder builder = new StringBuilder();
		final byte[] buffer = new byte[1024];
		while (true) {
			final int count = stream.read(buffer);
			if (count < 0) break;
			for (int i = 0; i < count; i++) {
				final char c = (char)buffer[i];
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Parse out the coordinates using given response.
	 * @param response The <code>String</code> XML
	 * response.
	 * @return The <code>Coordinates</code> value.
	 */
	private Coordinates parseCoordinates(final String response) {
		// Parse out location tag.
		final String locationOpenTag = this.buildOpenTag(this.locationTag);
		final String locationEndTag = this.buildEndTag(this.locationTag);
		final int locstart = response.indexOf(locationOpenTag) + locationOpenTag.length();
		final int locend = response.indexOf(locationEndTag);
		if (locstart < 0 || locend < 0) return null;
		final String location = response.substring(locstart, locend);
		// Parse out latitude.
		final String latitudeOpenTag = this.buildOpenTag(this.latitudeTag);
		final String latitudeEndTag = this.buildEndTag(this.latitudeTag);
		final int latstart = location.indexOf(latitudeOpenTag) + latitudeOpenTag.length();
		final int latend = location.indexOf(latitudeEndTag);
		if (latstart < 0 || latend < 0) return null;
		final double latitude = Double.valueOf(location.substring(latstart, latend));
		// Parse out longitude.
		final String longitudeOpenTag = this.buildOpenTag(this.longitudeTag);
		final String longitudeEndTag = this.buildEndTag(this.longitudeTag);
		final int lngstart = location.indexOf(longitudeOpenTag) + longitudeOpenTag.length();
		final int lngend = location.indexOf(longitudeEndTag);
		if (lngstart < 0 || lngend < 0) return null;
		final double longitude = Double.valueOf(location.substring(lngstart, lngend));
		return new Coordinates(latitude, longitude);
	}
	
	/**
	 * Build the opening tag using the given tag name.
	 * @param tagname The <code>String</code> tag name.
	 * @return The <code>String</code> opening tag.
	 */
	private String buildOpenTag(final String tagname) {
		final StringBuilder builder = new StringBuilder();
		builder.append("<").append(tagname).append(">");
		return builder.toString();
	}
	
	/**
	 * Build the ending tag using the given tag name.
	 * @param tagname The <code>String</code> tag name.
	 * @return The <code>String</code> ending tag.
	 */
	private String buildEndTag(final String tagname) {
		final StringBuilder builder = new StringBuilder();
		builder.append("</").append(tagname).append(">");
		return builder.toString();
	}
	
	/**
	 * <code>Coordinates</code> defines the implementation
	 * of an immutable data structure representing a double
	 * precision latitude, longitude point.
	 *
	 * @author Yi Wang (Neakor)
	 * @version 1.0.0
	 */
	public final class Coordinates {
		/**
		 * The <code>double</code> latitude value.
		 */
		public final double latitude;
		/**
		 * The <code>double</code> longitude value.
		 */
		public final double longitude;
		
		/**
		 * Constructor of <code>Coordinates</code>.
		 * @param latitude The <code>double</code> latitude
		 * value.
		 * @param longitude The <code>double</code> longitude
		 * value.
		 */
		public Coordinates(final double latitude, final double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}
}
