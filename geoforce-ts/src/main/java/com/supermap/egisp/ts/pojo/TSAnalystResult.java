package com.supermap.egisp.ts.pojo;

import java.io.Serializable;

public class TSAnalystResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Point[] path;


	public Point[] getPath() {
		return path;
	}


	public void setPath(Point[] path) {
		this.path = path;
	}
	
	
	

}
