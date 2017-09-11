package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CfgRegion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_region", catalog = "egisp_dev")
public class Region extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String name;
	private Short status;
	private Short isShare;
	private Short source;
	private String userId;
	private String region;
	private String remark;
	private String eid;
	private String deptId;

	// Constructors

	/** default constructor */
	public Region() {
	}

	/** minimal constructor */
	public Region(String id) {
		this.id = id;
	}

	/** full constructor */
	public Region(String id, String name, Short status, Short isShare,
			Short source, String userId, String region, String remark,
			String eid, String deptId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.isShare = isShare;
		this.source = source;
		this.userId = userId;
		this.region = region;
		this.remark = remark;
		this.eid = eid;
		this.deptId = deptId;
	}


	@Column(name = "NAME")
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

	@Column(name = "IS_SHARE")
	public Short getIsShare() {
		return this.isShare;
	}

	public void setIsShare(Short isShare) {
		this.isShare = isShare;
	}

	@Column(name = "SOURCE")
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "REGION")
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "EID")
	public String getEid() {
		return this.eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	@Column(name = "DEPT_ID")
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

}