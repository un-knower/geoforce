package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;

/**
 * InfoDriver entity. @author MyEclipse Persistence Tools
 */

public class Driver implements java.io.Serializable {

	// Fields

	private String id;
	private String eid;
	private String deptId;
	private String name;
	private Short sex;
	private String license;
	private Short age;
	private Date licenseSdate;
	private Date licenseEdate;
	private String phone;
	private String address;
	private String zipcode;
	private String createUserid;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Driver() {
	}

	/** minimal constructor */
	public Driver(String id) {
		this.id = id;
	}

	/** full constructor */
	public Driver(String id,String eid, String deptId, String name, Short sex,
			String license, Short age, Date licenseSdate, Date licenseEdate,
			String phone, String address, String zipcode, String createUserid,
			Date modifyDate) {
		this.id = id;
		this.eid = eid;
		this.deptId = deptId;
		this.name = name;
		this.sex = sex;
		this.license = license;
		this.age = age;
		this.licenseSdate = licenseSdate;
		this.licenseEdate = licenseEdate;
		this.phone = phone;
		this.address = address;
		this.zipcode = zipcode;
		this.createUserid = createUserid;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

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

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	
	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	
	public Short getAge() {
		return this.age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	
	public Date getLicenseSdate() {
		return this.licenseSdate;
	}

	public void setLicenseSdate(Date licenseSdate) {
		this.licenseSdate = licenseSdate;
	}

	
	public Date getLicenseEdate() {
		return this.licenseEdate;
	}

	public void setLicenseEdate(Date licenseEdate) {
		this.licenseEdate = licenseEdate;
	}

	
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}