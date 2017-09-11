package com.supermap.egispapi.pojo;

import java.io.Serializable;

public class GeocodingParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String address;
	/**
	 * 高解析率
	 */
	private int level = 1;
	/**
	 * 行政区协助查询
	 */
	private int qs = 3;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getQs() {
		return qs;
	}
	public void setQs(int qs) {
		this.qs = qs;
	}
	
	
}
