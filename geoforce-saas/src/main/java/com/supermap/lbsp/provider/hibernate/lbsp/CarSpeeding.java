package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;


/**
 * CfgCarSpeeding entity. @author MyEclipse Persistence Tools
 */

public class CarSpeeding implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private String id;
	private String name;
	private Short status;//0 未启用      1启用
	private String remark;
	private Float speed;
	private String week;
	private String startTime;
	private String endTime;
	private Date operTime;

	// Constructors

	/** default constructor */
	public CarSpeeding() {
	}

	/** minimal constructor */
	public CarSpeeding(String id) {
		this.id = id;
	}

	/** full constructor */
	public CarSpeeding(String id, String name, Short status, String remark,
			Float speed, String week, String startTime, String endTime,
			Date operTime) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.remark = remark;
		this.speed = speed;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operTime = operTime;
	}

	// Property accessors
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public Float getSpeed() {
		return this.speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	
	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

}