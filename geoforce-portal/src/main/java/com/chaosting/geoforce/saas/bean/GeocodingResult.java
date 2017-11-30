package com.chaosting.geoforce.saas.bean;

import java.io.Serializable;

public class GeocodingResult implements Serializable {

	private static final long serialVersionUID = -8194384515190459081L;

	private String id;

	private String address;

	private String x;

	private String y;

	public GeocodingResult(String id, String address, String x, String y) {
		this.id = id;
		this.address = address;
		this.x = x;
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

}
