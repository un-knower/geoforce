package com.supermap.lbsp.provider.hibernate.info;

import java.util.Date;


/**
 * 
 */

public class Company implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String address;
	private String email;
	private String phone;
	private String remark;
	private Short statusId;
	private Date createTime;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public Company() {
	}

	/** minimal constructor */
	public Company(String id) {
		this.id = id;
	}

	/** full constructor */
	public Company(String id, String name, String address, String email,
			String phone, String remark, Short statusId, Date createTime,
			Date updateTime) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.remark = remark;
		this.statusId = statusId;
		this.createTime = createTime;
		this.updateTime = updateTime;
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

	
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public Short getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Short statusId) {
		this.statusId = statusId;
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

}