package org.sunil.ufl.service;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sunil.ufl.dto.FilmLocation;
import org.sunil.ufl.jest.JestClientSingleton;

@Component
public class FilmLocationQueryHandler {
	private Logger LOG = LoggerFactory.getLogger(FilmLocationQueryHandler.class);
	
	/**
	 * This API returns the names of movies which matches the given filmName for autocomplete.
	 * 
	 * If the given filmName is empty, then it returns the first 20 film names sorted alphabetically.
	 * 
	 * @param filmName
	 * @return Set<String>
	 */
	public Set<String> getFilmNamesLike(String filmName) {
		Set<String> filmNames = new LinkedHashSet<>();
		
		try {
			JestClient client = JestClientSingleton.getInstance().getJestClient();
			
			QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
			
			if (StringUtils.isNotBlank(filmName)) {
				queryBuilder = QueryBuilders.matchQuery("title", filmName);
			}
			
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	        searchSourceBuilder.query(queryBuilder);
	        searchSourceBuilder.size(20);
	        searchSourceBuilder.sort(SortBuilders.fieldSort("title").order(SortOrder.ASC));
	        
	        if (LOG.isInfoEnabled()) {
	        	LOG.info("Query to get filmNamesLike - {} - {}", filmName, searchSourceBuilder.toString());
	        }

	        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(FilmLocationIndexerService.getIndexName()).build();

	        SearchResult result = null;
	        
	        result = client.execute(search);
	       
	        if (result.isSucceeded()) {
	            List<Hit<FilmLocation, Void>> hits = result.getHits(FilmLocation.class);
	            
	            if (CollectionUtils.isNotEmpty(hits)) {
	            	for (Hit<FilmLocation, Void> hit : hits) {
	            		FilmLocation location = hit.source;
	            		
	            		filmNames.add(location.getTitle());
	            	}
	            }
	        }
			
		} catch (Exception exception) {
			LOG.error("Error getting film names that are like {} - error {}", filmName, exception.getMessage(), exception);
		}
		
		return filmNames;
	}
	
	public Set<FilmLocation> getFilmLocationsFor(String filmName) {
		Validate.notBlank(filmName);
		
		Set<FilmLocation> filmLocations = new LinkedHashSet<>();
		
		try {
			JestClient client = JestClientSingleton.getInstance().getJestClient();
			
			QueryBuilder queryBuilder = QueryBuilders.matchQuery("title.raw", filmName);
			
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	        searchSourceBuilder.query(queryBuilder);
	        searchSourceBuilder.size(100);
	        
	        if (LOG.isInfoEnabled()) {
	        	LOG.info("Query to get filmLocations for film - {} - {}", filmName, searchSourceBuilder.toString());
	        }

	        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(FilmLocationIndexerService.getIndexName()).build();

	        SearchResult result = null;
	        
	        result = client.execute(search);
	       
	        if (result.isSucceeded()) {
	            List<Hit<FilmLocation, Void>> hits = result.getHits(FilmLocation.class);
	            
	            if (CollectionUtils.isNotEmpty(hits)) {
	            	for (Hit<FilmLocation, Void> hit : hits) {
	            		FilmLocation location = hit.source;
	            		
	            		filmLocations.add(location);
	            	}
	            }
	        }
			
		} catch (Exception exception) {
			LOG.error("Error getting film names that are like {} - error {}", filmName, exception.getMessage(), exception);
		}
		
		return filmLocations;
	}
}
