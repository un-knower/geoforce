package com.supermap.egispservice.base.entity;

import java.io.Serializable;

/**
 * 
 * @description 点（supermap objects java 的 Point2D不能序列化）
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-13
 * @version 1.0
 */
public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double lon;
	private double lat;
	public Point() {
		super();
	}
	public Point(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	@Override
	public String toString() {
		return "Point("+lon+" "+lat+")";
	}
	
}
