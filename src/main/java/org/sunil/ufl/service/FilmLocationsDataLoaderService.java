package org.sunil.ufl.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.sunil.ufl.dto.FilmLocation;
import org.sunil.ufl.dto.Location;
import org.sunil.ufl.utils.HashUtils;
import org.sunil.ufl.utils.LocationUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * This service will load the film locations data from 
 * 
 * https://data.sfgov.org/resource/wwmu-gmzc.json
 * 
 * @author sunil
 *
 */
@Component
public class FilmLocationsDataLoaderService {
	private Logger LOG = LoggerFactory.getLogger(FilmLocationsDataLoaderService.class);
	
	// Normally I would put this URL in a config file (like Apache commons config)
	public static final String DATA_URL = "https://data.sfgov.org/resource/wwmu-gmzc.json";
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * This API will load the film locations from the DATA_URL.
	 * 
	 * Assumption : Data is small in size (less then 5000 items). If data is large, then I will have paging mechanism
	 * to load the data. Since we are using a public API, and I already know that the dataset is small, I am loading all
	 * the data in one shot.
	 * 
	 * @return List<FilmLocation>
	 */
	public List<FilmLocation> getFilmLocations() {
		List<FilmLocation> locations = new ArrayList<>();
		try {
			if (LOG.isInfoEnabled()) {
				LOG.info("Getting film locations from {}", DATA_URL);
			}
			
			RestTemplate template = new RestTemplate();
			
			List results = template.getForObject(DATA_URL, List.class);
			
			if (CollectionUtils.isNotEmpty(results)) {
				
				if (LOG.isInfoEnabled()) {
					LOG.info("Got {} film locations from {}", results.size(), DATA_URL);
				}
				
				for (Object obj : results) {
					FilmLocation location = mapper.convertValue(obj, FilmLocation.class);
					
					// Skip loations that are null or have empty locations.
					if (location != null && StringUtils.isNotEmpty(location.getLocations())) {
						
						// This can also be a hash of title + locations + director
						// Using UUID for this.
						location.setId(HashUtils.generateHash(location.getTitle()+":"+location.getLocations()));
						
						// For each location, reverse geo code the address to get the Lat and Long
						Location latLng = LocationUtils.getLocationFromAddress(location.getLocations());
						// If location cannot be obtained, skip it.
						if (latLng == null) {
							continue;
						}
						
						location.setLocation(latLng);
						
						locations.add(location);
					}
				}
				
				if (LOG.isInfoEnabled()) {
					LOG.info("returning {} valid film locations from {}", locations.size(), DATA_URL);
				}
			} else {
				LOG.error("Could not find any locations at - {}", DATA_URL);
			}
		} catch (Exception exception) {
			LOG.error("Error when loading the film locations from URL {} - error -{}", DATA_URL, exception.getMessage(), exception);
		}
		
		return locations;
	}
	
	
}
