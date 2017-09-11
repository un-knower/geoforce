package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;

/**
 * CfgRegionSet entity. @author MyEclipse Persistence Tools
 */

public class RegionSet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private String id;
	private String deptId;
	private String regionId;
	private String urserId;
	private Integer alarmType;
	private Short moduleType;
	private String week;
	private String startTime;
	private String endTime;
	private Date operDate;

	// Constructors

	/** default constructor */
	public RegionSet() {
	}

	/** minimal constructor */
	public RegionSet(String id) {
		this.id = id;
	}

	/** full constructor */
	public RegionSet(String id, String deptId, String regionId,
			String urserId, Integer alarmType, Short moduleType, String week,
			String startTime, String endTime, Date operDate) {
		this.id = id;
		this.deptId = deptId;
		this.regionId = regionId;
		this.urserId = urserId;
		this.alarmType = alarmType;
		this.moduleType = moduleType;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operDate = operDate;
	}

	// Property accessors
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	
	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	
	public String getUrserId() {
		return this.urserId;
	}

	public void setUrserId(String urserId) {
		this.urserId = urserId;
	}

	
	public Integer getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	
	public Short getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(Short moduleType) {
		this.moduleType = moduleType;
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

	
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}