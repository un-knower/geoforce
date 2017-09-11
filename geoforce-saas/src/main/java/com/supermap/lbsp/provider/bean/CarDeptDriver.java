package com.supermap.lbsp.provider.bean;

import java.util.Date;


public class CarDeptDriver implements java.io.Serializable{
	/**
	 * 
	 */
	private String id;//司机id
	private String name;//司机名称
	private String deptId;

	private String license;//驾驶证号码
	private String deptName;//部门名称
	private String licenseDate;//驾驶证有效期
	private String carNames;
	private String phone;
	private Date modifyDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getLicenseDate() {
		return licenseDate;
	}
	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}
	public String getCarNames() {
		return carNames;
	}
	public void setCarNames(String carNames) {
		this.carNames = carNames;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	


}
