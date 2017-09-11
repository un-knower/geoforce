package com.supermap.egispapi.pojo;

import java.io.Serializable;

public class CoorConvertParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String coords;
	private String from ;
	private String to;
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	
}
