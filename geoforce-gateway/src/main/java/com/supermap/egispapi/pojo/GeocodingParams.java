package com.supermap.egispapi.pojo;

import java.io.Serializable;
import java.util.List;

public class GeocodingParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<GeocodingParam> addresses;
	private String type;
	public List<GeocodingParam> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<GeocodingParam> addresses) {
		this.addresses = addresses;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
