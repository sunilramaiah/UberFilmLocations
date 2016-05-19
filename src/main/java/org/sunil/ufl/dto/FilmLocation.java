package org.sunil.ufl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Bean for FilmLocation
 * 
 * @author sunil
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_NULL)
public class FilmLocation {
	private String id;
	private String title;
	private String release_year;
	private String locations;
	private String production_company;
	private String fun_facts;
	private String distributor;
	private String director;
	private String writer;
	private String actor_1;
	private String actor_2;
	private String actor_3;

	private Location location;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelease_year() {
		return release_year;
	}

	public void setRelease_year(String release_year) {
		this.release_year = release_year;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getProduction_company() {
		return production_company;
	}

	public void setProduction_company(String production_company) {
		this.production_company = production_company;
	}

	public String getFun_facts() {
		return fun_facts;
	}

	public void setFun_facts(String fun_facts) {
		this.fun_facts = fun_facts;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getActor_1() {
		return actor_1;
	}

	public void setActor_1(String actor_1) {
		this.actor_1 = actor_1;
	}

	public String getActor_2() {
		return actor_2;
	}

	public void setActor_2(String actor_2) {
		this.actor_2 = actor_2;
	}

	public String getActor_3() {
		return actor_3;
	}

	public void setActor_3(String actor_3) {
		this.actor_3 = actor_3;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "FilmLocation [id=" + id + ", title=" + title + ", release_year=" + release_year + ", locations=" + locations + ", production_company=" + production_company + ", fun_facts="
				+ fun_facts + ", distributor=" + distributor + ", director=" + director + ", writer=" + writer + ", actor_1=" + actor_1 + ", actor_2=" + actor_2 + ", actor_3=" + actor_3
				+ ", location=" + location + "]";
	}
	
}
