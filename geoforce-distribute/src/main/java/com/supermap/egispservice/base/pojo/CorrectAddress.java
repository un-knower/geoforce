package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.Date;

public class CorrectAddress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String address;
	private String batch;
	private String areaNum;
	private String areaName;
	private String desc;
	private Date addTime;
	private Date correctTime;
	private int status;
	private double x;
	private double y;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getCorrectTime() {
		return correctTime;
	}
	public void setCorrectTime(Date correctTime) {
		this.correctTime = correctTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

}
