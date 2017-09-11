package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;

/**
 * InfoTerminal entity. @author MyEclipse Persistence Tools
 */

public class Terminal implements java.io.Serializable {

	// Fields

	private String id;
	private String eid;
	private String typeId;
	private String carId;
	private String name;
	private String code;
	private String mobile;
	private String deptId;
	private Date operDate;

	// Constructors

	/** default constructor */
	public Terminal() {
	}

	/** full constructor */
	public Terminal(String id,String eid, String typeId, String carId, String name,
			String code, String mobile, String deptId, Date operDate) {
		this.id = id;
		this.eid = eid;
		this.typeId = typeId;
		this.carId = carId;
		this.name = name;
		this.code = code;
		this.mobile = mobile;
		this.deptId = deptId;
		this.operDate = operDate;
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

	
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}