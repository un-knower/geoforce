package com.supermap.egispweb.pojo.export;

import java.util.Date;

public class Alarm {
	private String carLicense;
	private String deptName;
	private String alarmName;
	private Date alarmDate;
	private Long difTime;
	private String typeName;
	private String addr;
	private Double speed;
	private String stautsName;
	private String userName;
	private Date dealDate;
	public String getCarLicense() {
		return carLicense;
	}
	public void setCarLicense(String carLicense) {
		this.carLicense = carLicense;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getAlarmName() {
		return alarmName;
	}
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}
	public Date getAlarmDate() {
		return alarmDate;
	}
	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	public Long getDifTime() {
		return difTime;
	}
	public void setDifTime(Long difTime) {
		this.difTime = difTime;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public String getStautsName() {
		return stautsName;
	}
	public void setStautsName(String stautsName) {
		this.stautsName = stautsName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getDealDate() {
		return dealDate;
	}
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}
}
