package org.convert.services;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.convert.utils.JacksonHelper;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Using GeoNames REST API to get location/time information.
 * Using my account (deffer) to access free service.
 *
 * author: Irina Benediktovich - http://plus.google.com/+IrinaBenediktovich
 */
public class GeoNamesWeb implements GeoLocationService {

	// TODO move to properties file as http://api.geonames.org/timezoneJSON?lat={lat}&lng={lng}&username=demo
	String GEO_NAMES_URL = "http://api.geonames.org/timezoneJSON";

	@Override
	public GeoLocationInfo getGeoLocationInfo(String lat, String lng) throws GeoLocationException {
		try {
			return makeRequest(lat,lng);
		} catch (IOException e) {
			throw new GeoLocationException("Connection error", e);
		}
	}

	private GeoLocationInfo makeRequest(String lat, String lng) throws IOException, GeoLocationException {

		String urlString = GEO_NAMES_URL + "?lat=" + lat + "&lng=" + lng+"&username=deffer";
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		List<String> lines = IOUtils.readLines(conn.getInputStream());
		String body = StringUtils.join(lines, " ");
		//System.out.println(body);
		try {
			GeoNamesResponse data = JacksonHelper.deserialize(body, GeoNamesResponse.class);
			return new GeoLocationInfo(data.timezoneId, data.countryName);
		} catch (UnrecognizedPropertyException e){
			if (e.getMessage().contains("error parsing parameter"))
				throw new GeoLocationException("Error parsing coordinates", null);
			else
				throw new GeoLocationException("Account limit has been reached", e);
		} catch (Exception e) {
			throw new GeoLocationException("Unable to deserialize data", e);
		}
	}

	public static class GeoNamesResponse {
		public String time;
		public String countryName;
		public String countryCode;
		public String timezoneId;
		public String sunset;
		public String sunrise;
		public String rawOffset;
		public String dstOffset;
		public String gmtOffset;
		public String lng;
		public String lat;
	}
}
