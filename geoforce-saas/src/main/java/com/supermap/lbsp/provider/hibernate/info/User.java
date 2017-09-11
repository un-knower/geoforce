package com.supermap.lbsp.provider.hibernate.info;

import java.util.Date;


/**
 * EgispRssUser entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private String id;
	private String username;
	private String password;
	private String realname;
	private String mobilephone;
	private String email;
	private String telephone;
	private String fax;
	private String sex;
	private String address;
	private String zipCode;
	private String remark;
	private Short stratusId;
	private String createUser;
	private Date createTime;
	private Date updateTime;
	private String eid;
	private String deptId;
	private Short sourceId;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String id) {
		this.id = id;
	}

	/** full constructor */
	public User(String id, String username, String password,
			String realname, String mobilephone, String email,
			String telephone, String fax, String sex, String address,
			String zipCode, String remark, Short stratusId, String createUser,
			Date createTime, Date updateTime, String eid, String deptId,
			Short sourceId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.mobilephone = mobilephone;
		this.email = email;
		this.telephone = telephone;
		this.fax = fax;
		this.sex = sex;
		this.address = address;
		this.zipCode = zipCode;
		this.remark = remark;
		this.stratusId = stratusId;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.eid = eid;
		this.deptId = deptId;
		this.sourceId = sourceId;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	
	public String getMobilephone() {
		return this.mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public Short getStratusId() {
		return this.stratusId;
	}

	public void setStratusId(Short stratusId) {
		this.stratusId = stratusId;
	}

	
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	
	public Short getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Short sourceId) {
		this.sourceId = sourceId;
	}

}