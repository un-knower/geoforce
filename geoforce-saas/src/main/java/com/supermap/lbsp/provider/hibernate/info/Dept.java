package com.supermap.lbsp.provider.hibernate.info;

import java.util.Date;


/**
 * InfoDept entity. @author MyEclipse Persistence Tools
 */

public class Dept implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String headName;
	private String headPhone;
	private String address;
	private String phone;
	private String zipcode;
	private String parentId;
	private Short type;
	private String code;
	private String createUserid;
	private Date operDate;

	// Constructors

	/** default constructor */
	public Dept() {
	}

	/** minimal constructor */
	public Dept(String id, String name, String phone, Short type,
			String code, String createUserid, Date operDate) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.code = code;
		this.createUserid = createUserid;
		this.operDate = operDate;
	}

	/** full constructor */
	public Dept(String id, String name, String headName, String headPhone,
			String address, String phone, String zipcode, String parentId,
			Short type, String code, String createUserid, Date operDate) {
		this.id = id;
		this.name = name;
		this.headName = headName;
		this.headPhone = headPhone;
		this.address = address;
		this.phone = phone;
		this.zipcode = zipcode;
		this.parentId = parentId;
		this.type = type;
		this.code = code;
		this.createUserid = createUserid;
		this.operDate = operDate;
	}

	
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

	
	public String getHeadName() {
		return this.headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	
	public String getHeadPhone() {
		return this.headPhone;
	}

	public void setHeadPhone(String headPhone) {
		this.headPhone = headPhone;
	}

	
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}