package org.convert.services;

/**
 * This is to abstract implementation of resolving coordinates to timezone.
 *
 * Could be lookup in the dat file, call to internal in-house solution, call to a 3rd party, etc..
 *
 * Using String as lat/lng type to avoid conversion from String to double (when reading from file)
 * and back to String (when making request to web service).
 *
 * By accepting lat/lng as String, we trust caller to perform validation/escaping before passing it to us.
 *
 * author: Irina Benediktovich - http://plus.google.com/+IrinaBenediktovich
 */
public interface GeoLocationService {

	/**
	 * Should resolve lat/lng into time zone.
	 *
	 * By accepting lat/lng as String, we trust caller to perform validation/escaping before passing it to us
	 *
	 * @param lat latitude
	 * @param lng longitude
	 * @return result
	 * @throws GeoLocationException caller may want to react differently on connection exception vs other exceptions.
	 *   Inspect cause for source of exception.
	 */
	GeoLocationInfo getGeoLocationInfo(String lat, String lng) throws GeoLocationException;
}
