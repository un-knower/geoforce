package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * CfgCarSpeeding entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_speeding_set", catalog = "egisp_dev")
public class SpeedingSet  extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String name;
	private Short status;//0启用  1 未启用
	private String remark;
	private Float speed;
	private String week;
	private String startTime;
	private String endTime;
	private Date operTime;
	private String eid;
	private String userId;
	

	// Constructors

	/** default constructor */
	public SpeedingSet() {
	}

	/** minimal constructor */
	public SpeedingSet(String id) {
		this.id = id;
	}

	/** full constructor */
	public SpeedingSet(String id, String name, Short status, String remark,
			Float speed, String week, String startTime, String endTime,
			Date operTime,String eid,String userId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.remark = remark;
		this.speed = speed;
		this.week = week;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operTime = operTime;
		this.eid=eid;
		this.userId=userId;
	}


	@Column(name = "NAME", length = 48)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STATUS")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "SPEED", precision = 12, scale = 0)
	public Float getSpeed() {
		return this.speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
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
	@Column(name = "OPER_TIME", length = 10)
	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	@Column(name = "EID_ID", length = 48)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	@Column(name = "USER_ID", length = 48)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}