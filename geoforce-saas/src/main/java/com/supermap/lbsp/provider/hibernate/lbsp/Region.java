package com.supermap.lbsp.provider.hibernate.lbsp;



/**
 * CfgRegion entity. @author MyEclipse Persistence Tools
 */

public class Region implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private String id;
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

	
	public Short getIsShare() {
		return this.isShare;
	}

	public void setIsShare(Short isShare) {
		this.isShare = isShare;
	}

	
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}