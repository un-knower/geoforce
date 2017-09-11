package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;

public class CoorConvertResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Point> coords;

	public List<Point> getCoords() {
		return coords;
	}

	public void setCoords(List<Point> coords) {
		this.coords = coords;
	}
	
	

}
