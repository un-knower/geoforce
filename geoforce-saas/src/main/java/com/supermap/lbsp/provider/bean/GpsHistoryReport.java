package com.supermap.lbsp.provider.bean;

import java.util.Date;

/**
 * 轨迹统计javabean
* @ClassName: TrackReportBean
* @author WangYaJun
* @date 2013-8-23 上午15:46:48
 */
public class GpsHistoryReport implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String id;
	private String carId;
	private String license;
	private Double longitude;
	private Double latitude;
	private Double originalLon;
	private Double originalLat;
	private Double speed;
	private String direction;
	private String addr;
	private Date sysTime;
	private String gpsTime;
	private Double mile;
	private String imgpath;
	private Double oil;
	
	public Double getOil() {
		return oil;
	}
	public void setOil(Double oil) {
		this.oil = oil;
	}
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
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getOriginalLon() {
		return originalLon;
	}
	public void setOriginalLon(Double originalLon) {
		this.originalLon = originalLon;
	}
	public Double getOriginalLat() {
		return originalLat;
	}
	public void setOriginalLat(Double originalLat) {
		this.originalLat = originalLat;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public Date getSysTime() {
		return sysTime;
	}
	public void setSysTime(Date sysTime) {
		this.sysTime = sysTime;
	}
	public Double getMile() {
		return mile;
	}
	public void setMile(Double mile) {
		this.mile = mile;
	}
	
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}
	

	
}
