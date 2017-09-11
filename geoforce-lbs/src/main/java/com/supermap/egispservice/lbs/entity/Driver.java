package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * InfoDriver entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_driver", catalog = "egisp_dev")
public class Driver extends IdEntity  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
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

	

	@Column(name = "EID",nullable = true, length = 48)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Column(name = "DEPT_ID", length = 48)
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "NAME", length = 48)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX")
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	@Column(name = "LICENSE", length = 20)
	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Column(name = "AGE")
	public Short getAge() {
		return this.age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LICENSE_SDATE", length = 10)
	public Date getLicenseSdate() {
		return this.licenseSdate;
	}

	public void setLicenseSdate(Date licenseSdate) {
		this.licenseSdate = licenseSdate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LICENSE_EDATE", length = 10)
	public Date getLicenseEdate() {
		return this.licenseEdate;
	}

	public void setLicenseEdate(Date licenseEdate) {
		this.licenseEdate = licenseEdate;
	}

	@Column(name = "PHONE", length = 20)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "ADDRESS", length = 128)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "ZIPCODE", length = 8)
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "CREATE_USERID", length = 48)
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE")
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	
}