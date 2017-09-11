package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;

import javax.persistence.Column;

/**
 * RepCarDismsg entity. @author MyEclipse Persistence Tools
 */

public class CarDismsg implements java.io.Serializable {

	// Fields

	private String id;
	private String title;
	private String content;
	private Date sendDate;
	private Short status;
	private String carId;
	private String license;
	private String mobile;
	private Short type;
	private String deptId;
	private String userId;
	
	// Constructors

	/** default constructor */
	public CarDismsg() {
	}

	/** minimal constructor */
	public CarDismsg(String id) {
		this.id = id;
	}

	/** full constructor */
	public CarDismsg(String id,String deptId, String title, String content, Date sendDate,
			Short status, String carId, String mobile,Short type,String userId) {
		this.id = id;
		this.deptId = deptId;
		this.title = title;
		this.content = content;
		this.sendDate = sendDate;
		this.status = status;
		this.carId = carId;
		this.mobile = mobile;
		this.type = type;
		this.userId = userId;
	}

	// Property accessors
	
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
}