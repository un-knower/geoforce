package com.supermap.egispweb.pojo.export;

public class GpsHistory {
	private String license;
	private Double longitude;
	private Double latitude;
	private String direction;
	private Double speed;
	private Double mile;
	private String gpsTime;
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
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
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
}
