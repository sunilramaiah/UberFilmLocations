package org.sunil.ufl.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sunil.ufl.dto.FilmLocation;
import org.sunil.ufl.dto.Result;
import org.sunil.ufl.jest.JestClientSingleton;
import org.sunil.ufl.jest.JestHelper;

@Component
public class FilmLocationIndexerService {
	private Logger LOG = LoggerFactory.getLogger(FilmLocationIndexerService.class);

	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	
	/**
	 * This API will index all the given locations.
	 * 
	 * Returns Result with details on the locations that were not indexed.
	 * 
	 * Result data will have "FAILED" key mapped to a List<LocationEntity>
	 * objects that failed. Result data will have "SUCCESS" key mapped to a
	 * List<LocationEntity> objects that succeeded with indexing.
	 * 
	 * @param locations
	 * @return Result
	 */
	public Result indexFilmLocations(List<FilmLocation> locations) {
		Validate.notNull(locations);

		Result result = new Result();
		
		result.getData().put(SUCCESS, new ArrayList<FilmLocation>());
		result.getData().put(FAILED, new ArrayList<FilmLocation>());
		
		try {
			boolean exists = createIndexIfNotExists();

			if (!exists) {
				result.setSuccess(false);
				result.setErrorCode("50001");
				result.setErrorMessage("Index could not be created with name " + getIndexName());
				
				return result;
			}
			
			for (FilmLocation location : locations) {
				// Index the location.
				boolean indexed = indexFilmLocation(location);

				// add it to the success/failure result.
				if (indexed) {
					((List) result.getData().get(SUCCESS)).add(location);
				} else {
					((List) result.getData().get(FAILED)).add(location);
				}
			}

			result.setSuccess(true);
		} catch (Exception exception) {
			LOG.error("Error when indexing locations - {}", exception.getMessage(), exception);
			result.setSuccess(false);
			result.setErrorCode("5000");
			result.setErrorMessage(exception.getMessage());
		}

		return result;
	}

	private boolean indexFilmLocation(FilmLocation location) {
		try {
			JestClient client = JestClientSingleton.getInstance().getJestClient();
	
			Index index = new Index.Builder(location).index(getIndexName()).type(getType()).id(location.getId()).build();
			JestResult jestResult = client.execute(index);
	
			if (LOG.isInfoEnabled()) {
				LOG.info("Indexed location - " + location.getId());
			}
	
			if (jestResult.isSucceeded()) {
				return true;
			} else {
				LOG.error("JestClient failed when indexing location - {} - reason - {}", location, jestResult.getErrorMessage());	
			}	
		} catch (Exception exception) {
			LOG.error("Error when indexing location - {} - error {}", location, exception.getMessage(), exception);
		}
		
		return false;
	}

	private boolean createIndexIfNotExists() throws Exception {
		JestClient client = JestClientSingleton.getInstance().getJestClient();

		if (!JestHelper.indexExists(client, getIndexName())) {
			String schema = JestHelper.getSchemaAsString(getSchemaName());
			
			CreateIndex createIndex = new CreateIndex.Builder(getIndexName()).settings(schema).build();

			JestResult result = client.execute(createIndex);
			
			if (result.isSucceeded()) {
				if (LOG.isInfoEnabled()) {
					LOG.info("Successfully created the index - {}", getIndexName());
				}
				return true;
			} else {
				LOG.error("Index {} creation failed - reason {}", getIndexName(), result.getErrorMessage());
				return false;
			}
		}
		
		return true;
	}

	/*
	 * This would be in a config file.
	 */
	public static String getIndexName() {
		return "filmlocations";
	}
	
	/**
	 * This would be a config file.
	 * @return String
	 */
	private String getSchemaName() {
		return "filmlocations";
	}
	
	/*
	 * This would be in a config file.
	 */
	private String getType() {
		return "filmlocation";
	}
}
