# UberFilmLocations

# Problem

Create a service that shows on a map where movies have been filmed in San Francisco. The user should be able to filter the view using autocompletion search.

This problem uses the data from https://data.sfgov.org/Culture-and-Recreation/Film-Locations-in-San-Francisco/yitu-d5am

# Solution

My solution focuses on full stack. To build this application, I have made the following technology choices -

Front-End: HTML / jQuery

Backend: Java / Spring REST Services / ElasticSearch

Though the role I am interviewing for is backend engineer, I have built a basic UI, referring to online examples and google maps API documentation. I have used AngularJS, ExtJS and jQuery before. For building a simple page with a maps and textfield, using AngularJS would be time consuming, as it takes time to setup the AngularJS project. Hence jQuery.

For backend, I am most comfortable with Java and I have been developing REST services using Spring for quite sometime now. I used elasticsearch as my data store and search index. ES makes it efficient for searches. I used edge ngram analyzer for indexing the movie titles so that I can do autocomplete in the UI when user is searching for the movie titles. Indexing the data in ES also provides benefit of searching by other fields as well. It would be easy to add new APIs to search by actors or directors or writers etc. Also, when loading the data from the above mentioned API, I used google geocoder to reverse geocode and get the latitude and longitude of the location and stored it in ES, so that the location can be plotted properly on the maps.

Tradeoffs

I would be writing JUnit tests and functional tests.

I have used JestClient to index and query ES. In a production system, I would not use it, but instead make it a worker node that joins the ES cluster, so that it will scale and performs best. JestClient does not scale well, but easier to setup.

I would also massage the data back to the client, by removing the unwanted data in the payload, so that the data transfer will be much faster b/w client and server. I am right now transferring all the data related to the film location back to the client.

For UI, I would use AngularJS or ReactJS and do better error handling in the UI. I have barebones of the UI app, but completely functional.

I would also be using Apache common configs to store all the API keys, server hostname / port, ES index names, schema names etc. and I would not be hardcoding these in the code normally.

# APIs

1. Index data

http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/ufl/index/filmlocations/seed

handled by 

FilmLocationsIndexController

2. Find the autocomplete names for movie (partial name)

http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/ufl/search/filmName/autocomplete?filmName=Bo

handled by 

FilmLocationsSearchController

3. Find all locations for the movie

http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/ufl/search/filmName?filmName=Boys%20and%20Girls

handled by 

FilmLocationsSearchController

# Demo

http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/



