package org.sunil.ufl.controller;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunil.ufl.dto.FilmLocation;
import org.sunil.ufl.dto.Result;
import org.sunil.ufl.service.FilmLocationQueryHandler;

@RestController
@RequestMapping("/search")
public class FilmLocationsSearchController {
	private Logger LOG = LoggerFactory.getLogger(FilmLocationsSearchController.class);
	
	@Autowired
	private FilmLocationQueryHandler queryHandler;
	
	/**
	 * This API takes in the filmName as a request parameter.
	 * 
	 * It returns Result.
	 * 
	 * If filmName is empty, then result will have an entry in the map with key "data" and values being list of film names, sorted alphabetically.
	 * 
	 * If filmName is not empty, then result will have an entry in the map with key "data" and values being the list of film names that match the given filmName.
	 * 
	 * 
	 * @param filmName
	 * @return Result
	 */
	@RequestMapping("/filmName/autocomplete")
	public Result getFilmNamesLike(@RequestParam(required=false, value="filmName") String filmName) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Request for autocomplete name - {}", filmName);
		}
		
		Result result = new Result();
		
		try {
			
			Set<String> filmNames = queryHandler.getFilmNamesLike(filmName);
			
			if (CollectionUtils.isNotEmpty(filmNames)) {
				result.setSuccess(true);
				result.getData().put("data", filmNames);
			} else {
				result.setSuccess(false);
				result.setErrorCode("6000");
				result.setErrorMessage("No results");
			}
			
		} catch (Exception exception) {
			LOG.error("Error getting autocomplete names for filmName - {} error {}", filmName, exception.getMessage(), exception);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setErrorMessage("Server error");
		}
		
		return result;
	}
	
	/**
	 * This API takes in a filmName request parameter that is required. Without the required parameter "filmName" this API will not be invoked.
	 * 
	 * Once the request is made, this API returns back a list of all locations where the given movie was shot in the Result with data map having key "data" 
	 * 
	 * @param filmName
	 * @return Result
	 */
	@RequestMapping("/filmName")
	public Result getFilmLoationsFor(@RequestParam(required=true, value="filmName") String filmName) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Request for autocomplete name - {}", filmName);
		}
		
		Result result = new Result();
		
		try {
			
			Set<FilmLocation> filmLocations = queryHandler.getFilmLocationsFor(filmName);
			
			if (CollectionUtils.isNotEmpty(filmLocations)) {
				result.setSuccess(true);
				result.getData().put("data", filmLocations);
			} else {
				result.setSuccess(false);
				result.setErrorCode("6000");
				result.setErrorMessage("No results");
			}
			
		} catch (Exception exception) {
			LOG.error("Error getting autocomplete names for filmName - {} error {}", filmName, exception.getMessage(), exception);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setErrorMessage("Server error");
		}
		
		return result;
	}
}
