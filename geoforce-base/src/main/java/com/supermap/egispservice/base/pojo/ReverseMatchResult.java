package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class ReverseMatchResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double smx;
	private double smy;
	private double distande;
	private String poi_id;
	private String address;
	private String province;
	private String city;
	private String county;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSmx() {
		
		return smx;
	}
	public void setSmx(double smx) {
		this.smx = smx;
	}
	public double getSmy() {
		return smy;
	}
	public void setSmy(double smy) {
		this.smy = smy;
	}
	public double getDistande() {
		return distande;
	}
	public void setDistande(double distande) {
		this.distande = distande;
	}
	public String getPoi_id() {
		return poi_id;
	}
	public void setPoi_id(String poiId) {
		poi_id = poiId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
	

}
