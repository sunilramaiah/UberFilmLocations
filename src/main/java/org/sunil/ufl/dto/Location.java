package org.sunil.ufl.dto;

import java.math.BigDecimal;

public class Location {
	private BigDecimal lat;
	private BigDecimal lon;

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

}
