package com.supermap.egispweb.pojo;

import java.util.Date;


public class OilLevelInfoBean implements  java.io.Serializable {
	
	/**
	 * 
	 */
	private String id;
	private String carId;
	private Double oil;
	private Double mile;
	private String gpsTime;
	private String license;
	private String gpsTimeStr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public Double getOil() {
		return oil;
	}
	public void setOil(Double oil) {
		this.oil = oil;
	}
	public Double getMile() {
		return mile;
	}
	public void setMile(Double mile) {
		this.mile = mile;
	}

	
	public String getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getGpsTimeStr() {
		return gpsTimeStr;
	}
	public void setGpsTimeStr(String gpsTimeStr) {
		this.gpsTimeStr = gpsTimeStr;
	}
	
	

}
