package com.supermap.egispapi.pojo;

import java.io.Serializable;

public class AreaQueryParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String number;
	private boolean includPoints;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isIncludPoints() {
		return includPoints;
	}
	public void setIncludPoints(boolean includPoints) {
		this.includPoints = includPoints;
	}
	
	@Override
	public String toString() {
		return "id:"+this.id+",name:"+this.name+",number:"+this.number+",includPoints:"+this.includPoints;
	}
	
	
}
