package com.supermap.egispservice.base.entity;

import java.io.Serializable;

/**
 * 
  * @ClassName: POIInfo
  * @Description: 
  *   POI对象  
  * @author huanghuasong
  * @date 2016-2-17 下午2:19:02
  *
 */
public class POIInfo implements Serializable,Comparable<POIInfo> {

	/**
	  * @Fields serialVersionUID : 
	  */
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String province;
	private String city;
	private String county;
	private String town;
	private String name;
	private String address;
	private double lon;
	private double lat;
	private String admincode;
	private double distance = Double.MAX_VALUE;
	
	@Override
	public String toString() {
		return this.id + "," + this.province + "," + this.city + ","
				+ this.county + "," + this.name + "," + this.address + ","
				+ this.lon + "," + this.lat + "," + this.admincode+","+this.town+","+","+this.distance;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	public int compareTo(POIInfo o) {
		return this.distance > o.getDistance() ? 1 : -1;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
	
	
	

}
