package org.sunil.ufl.utils;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sunil.ufl.dto.Location;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

/**
 * This is a utility class with APIs for geocoding and reverse geocoding.
 * 
 * @author sunil
 *
 */
public final class LocationUtils {
	private static Logger LOG = LoggerFactory.getLogger(LocationUtils.class);
	
	/**
	 * This API will return the location with reverse geo coded lat and long using Geocoder
	 * for the given address.
	 * 
	 * If there is any error with geocoder, this API will return null.
	 * 
	 * @param address
	 * @return Location
	 */
	public static Location getLocationFromAddress(String address) {
		Validate.notBlank(address);
		
		Location location = getLocation(address);
		
		if (location == null) {
			// Could not get location. 
			// If address has the format name(address), then strip address.
			int startBraceIndex = address.indexOf("(");
			int endBraceIndex = address.indexOf(")");
			if ( startBraceIndex > -1 && endBraceIndex > -1) {
				String stripped = address.substring(startBraceIndex + 1, endBraceIndex);
				
				location = getLocation(stripped);
			}
		}
		
		return location;
	}
	
	
	private static Location getLocation(String address) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address+", San Francisco, CA, USA").setLanguage("en").getGeocoderRequest();
			
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			List<GeocoderResult> resultList = geocoderResponse.getResults();
			for(GeocoderResult r : resultList)
			{
				GeocoderGeometry gg = r.getGeometry();
				BigDecimal lat = gg.getLocation().getLat();
				BigDecimal lng = gg.getLocation().getLng();
				
				Location location = new Location();
				location.setLat(lat);
				location.setLon(lng);
				
				return location;
			}
		} catch (Exception exception) {
			LOG.error("Error when getting lat and long for address - {}", address, exception.getMessage(), exception);
		}
		
		return null;
	}
}
