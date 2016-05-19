package org.sunil.ufl.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunil.ufl.dto.FilmLocation;
import org.sunil.ufl.dto.Result;
import org.sunil.ufl.service.FilmLocationIndexerService;
import org.sunil.ufl.service.FilmLocationsDataLoaderService;

@RestController
@RequestMapping("/index")
public class FilmLocationsIndexController {
	private Logger LOG = LoggerFactory.getLogger(FilmLocationsIndexController.class);
	
	@Autowired
	private FilmLocationsDataLoaderService dataLoaderService;
	
	@Autowired
	private FilmLocationIndexerService indexerService;
	
	/**
	 * This API will load the film locations from https://data.sfgov.org/resource/wwmu-gmzc.json.
	 * For each location, if address is valid, it will reverse geo-code and find the lat lng for each
	 * film location and indexes it in elastic search.
	 * 
	 * It returns a Result with information on which locations were indexed and which were not.
	 * 
	 * @return Result.
	 */
	@RequestMapping("/filmlocations/seed")
	public Result seedFilmLocations() {
		try {
			List<FilmLocation> locations = dataLoaderService.getFilmLocations();
			
			Result indexResult = indexerService.indexFilmLocations(locations);
		
			return indexResult;
		} catch (Exception exception) {
			LOG.error("Error seeding the film locations - {}", exception.getMessage(), exception);
			Result result = new Result();
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setErrorMessage("Server error when indexing film locations");
			return result;
		}
	}
}
