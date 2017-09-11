package com.supermap.egispweb.pojo;

import java.io.Serializable;

public class AlarmRemind implements Serializable{
	private String alarmTime;
	private String license;
	private String temCode;
	private String alarmType;
	/**未处理报警数量*/
	private int count;
	private String addr;//报警位置
	private String others;
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getTemCode() {
		return temCode;
	}
	public void setTemCode(String temCode) {
		this.temCode = temCode;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	
}
