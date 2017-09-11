package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * CfgRegionSet entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_region_set", catalog = "egisp_dev")
public class RegionSet extends IdEntity  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String deptId;
	//private String regionId;
	private String urserId;
	private Integer alarmType;//alarmTypeCode
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
		//this.regionId = regionId;
		this.urserId = urserId;
		this.alarmType = alarmType;
		this.moduleType = moduleType;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operDate = operDate;
	}


	@Column(name = "DEPT_ID", length = 48)
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/*@Column(name = "REGION_ID", length = 48)
	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}*/

	@Column(name = "URSER_ID", length = 48)
	public String getUrserId() {
		return this.urserId;
	}

	public void setUrserId(String urserId) {
		this.urserId = urserId;
	}

	@Column(name = "ALARM_TYPE")
	public Integer getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	@Column(name = "MODULE_TYPE")
	public Short getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(Short moduleType) {
		this.moduleType = moduleType;
	}

	@Column(name = "WEEK", length = 48)
	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	@Column(name = "START_TIME", length = 24)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 24)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPER_DATE", length = 10)
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
	private Region regionId;

	
	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="REGION_ID")
	public Region getRegionId() {
		return regionId;
	}

	public void setRegionId(Region regionId) {
		this.regionId = regionId;
	}
	

}