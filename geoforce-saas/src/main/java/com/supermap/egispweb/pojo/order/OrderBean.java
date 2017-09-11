package com.supermap.egispweb.pojo.order;

public class OrderBean {

	private String number;
	private String address;
	
	private String startTime;
	private String endTime;
	
	private String requirements;//需求量
	
	private double smx;//坐标x
	private double smy;//坐标y
	
	
	
	

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

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public OrderBean(String number, String address) {
		this.number = number;
		this.address = address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
