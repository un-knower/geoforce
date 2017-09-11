package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * LbsPersonPlan entity. @author MyEclipse Persistence Tools
 */
public class PersonPlan implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private String eid;
	private String deptId;
	private String personId;
	private String storeId;
	private Short type;
	private String opinion;
	private Short status;
	private String content;
	private Date begintdate;
	private Date sureDate;
	private Date operDate;
	private String userId;

	// Constructors

	/** default constructor */
	public PersonPlan() {
	}

	/** minimal constructor */
	public PersonPlan(String id, String eid, String deptId, String storeId,
			Short type, Short status, String content, Date begintdate,
			Date operDate, String userId) {
		this.id = id;
		this.eid = eid;
		this.deptId = deptId;
		this.storeId = storeId;
		this.type = type;
		this.status = status;
		this.content = content;
		this.begintdate = begintdate;
		this.operDate = operDate;
		this.userId = userId;
	}

	/** full constructor */
	public PersonPlan(String id, String eid, String deptId, String personId,
			String storeId, Short type, String opinion, Short status,
			String content, Date begintdate, Date sureDate,
			Date operDate, String userId) {
		this.id = id;
		this.eid = eid;
		this.deptId = deptId;
		this.personId = personId;
		this.storeId = storeId;
		this.type = type;
		this.opinion = opinion;
		this.status = status;
		this.content = content;
		this.begintdate = begintdate;
		this.sureDate = sureDate;
		this.operDate = operDate;
		this.userId = userId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEid() {
		return this.eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getBegintdate() {
		return this.begintdate;
	}

	public void setBegintdate(Date begintdate) {
		this.begintdate = begintdate;
	}

	public Date getSureDate() {
		return this.sureDate;
	}

	public void setSureDate(Date sureDate) {
		this.sureDate = sureDate;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}